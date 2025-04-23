package app.service;

import app.dto.request.PaymentGatewayRequest;
import app.dto.request.ProcessCheckoutRequest;
import app.dto.response.ApiResponse;
import app.entity.Reservation;
import app.entity.Space;
import app.entity.User;
import app.util.exception.PaymentNotProcessedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class CheckoutService {
    private final ReservationService reservationService;
    private final SpaceService spaceService;

    @Autowired
    public CheckoutService(ReservationService reservationService, SpaceService spaceService) {
        this.reservationService = reservationService;
        this.spaceService = spaceService;
    }

    @Async
    public CompletableFuture<ApiResponse> executeCheckout(
            User user, String token, Space space, ProcessCheckoutRequest request) {
        PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest();
        paymentRequest.setToken(token);
        paymentRequest.setEmail(request.getEmail());
        paymentRequest.setCvc(request.getCvc());
        paymentRequest.setAmount(space.getPrice());

        try (HttpClient httpClient = HttpClient.newBuilder().build()) {
            CompletableFuture<HttpResponse<String>> response =
                    fetchPaymentGatewayApi(httpClient, paymentRequest);

            if (response.get().statusCode() == 200) {
                reservationService.create(user, space, request.getDate(), request.getHour(), token);
                return CompletableFuture.completedFuture(new ApiResponse(200, "Success"));
            } else {
                return CompletableFuture.completedFuture(
                        new ApiResponse(422, "Rejected by Payment Gateway"));
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(
                    new ApiResponse(422, "Exception thrown while fetching from Payment Gateway. " +
                            e.getMessage()));
        }
    }

    @Async
    public CompletableFuture<HttpResponse<String>> fetchPaymentGatewayApi(
            HttpClient httpClient, PaymentGatewayRequest requestBody) {
        String requestBodyStr = requestBody.toString();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create("http://localhost:8081/api/session"))
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyStr)).build();
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<ApiResponse> processCheckout(User user, ProcessCheckoutRequest request) {
        Space space = spaceService.getById(request.getSpaceId());
        String token = UUID.randomUUID().toString().substring(0, 16);

        return  executeCheckout(
                user,
                token,
                space,
                request);
    }

}
