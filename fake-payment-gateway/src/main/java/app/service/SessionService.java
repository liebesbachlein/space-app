package app.service;

import app.dto.BankRequestBody;
import app.dto.CreateSessionRequest;
import app.dto.CreateSessionResponse;
import app.entity.Session;
import app.repository.SessionRepository;
import app.util.CryptoConverter;
import app.util.exception.SessionInUseOrClosedException;
import app.util.exception.SessionNotExecutedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@Transactional
public class SessionService {
    private final SessionRepository sessionRepo;

    @Autowired
    public SessionService(SessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @Transactional
    public Session createSession(CreateSessionRequest request) {
        Session session = new Session();
        String encryptedEmail = CryptoConverter.convertToDatabaseColumn(
                request.getEmail(), request.getToken()).orElseThrow(RuntimeException::new);
        String encryptedCvc = CryptoConverter.convertToDatabaseColumn(
                request.getCvc(), request.getToken()).orElseThrow(RuntimeException::new);
        session.setEmail(encryptedEmail);
        session.setCvc(encryptedCvc);
        session.setToken(request.getToken());
        session.setStatus(Session.Status.CREATED);
        session.setAmount(request.getAmount());
        session.setCreatedAt(Instant.now());
        return sessionRepo.saveAndFlush(session);
    }

    @Async
    @Transactional
    public CompletableFuture<Session> executeSession(UUID sessionId) throws SessionInUseOrClosedException {
        Session session = sessionRepo.getReferenceById(sessionId);
        if (session.getStatus() != Session.Status.CREATED)
            throw new SessionInUseOrClosedException("Session with id " +
                    sessionId + " cannot be executed, because it's in use or closed");
        session.setStatus(Session.Status.PENDING);
        String decryptedEmail = CryptoConverter.convertToEntityAttribute(
                session.getEmail(), session.getToken()).orElseThrow(RuntimeException::new);
        String decryptedCvc = CryptoConverter.convertToEntityAttribute(
                session.getCvc(), session.getToken()).orElseThrow(RuntimeException::new);

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            CompletableFuture<HttpResponse<String>> response = fetchBankApi(httpClient, new BankRequestBody(
                    decryptedEmail, session.getAmount(), decryptedCvc));

            if (response.get().statusCode() == 200) {
                log.info("Payment transaction completed successfully by" +  decryptedEmail);
                session.setStatus(Session.Status.SUCCESS);
            } else {
                log.error("Failed to complete payment transaction by " + decryptedEmail);
                session.setStatus(Session.Status.FAIL);
            }
            return CompletableFuture.completedFuture(session);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred while completing payment transaction by " + decryptedEmail);
            session.setStatus(Session.Status.FAIL);
            //Thread.currentThread().interrupt();
        }

        sessionRepo.saveAndFlush(session);
        return CompletableFuture.completedFuture(session);
    }

    @Async
    public CompletableFuture<HttpResponse<String>> fetchBankApi(HttpClient httpClient, BankRequestBody requestBody) {
        String requestBodyStr = requestBody.toString();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create("http://localhost:8082/api/withdraw"))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyStr)).build();
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    @Async
    public CompletableFuture<CreateSessionResponse> processSession(CreateSessionRequest request)
            throws SessionInUseOrClosedException, SessionNotExecutedException {
        Session session = createSession(request);
        CompletableFuture<Session> executedSession = executeSession(session.getId());

        CompletableFuture<CreateSessionResponse> response = executedSession
                .thenApply((s) -> new CreateSessionResponse(s.getToken(),
                        s.getStatus() == Session.Status.SUCCESS));

        return response;
    }
}
