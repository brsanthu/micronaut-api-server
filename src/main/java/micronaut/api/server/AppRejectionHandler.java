package micronaut.api.server;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.handlers.HttpStatusCodeRejectionHandler;
import io.micronaut.security.handlers.RejectionHandler;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;

@Singleton
@Primary
@Replaces(HttpStatusCodeRejectionHandler.class)
public class AppRejectionHandler implements RejectionHandler {
    @Override
    public Publisher<MutableHttpResponse<?>> reject(HttpRequest<?> request, boolean forbidden) {
        String invalidSession = "{\"errorCode\": \"invalid_session\", \"message\": \"Invalid Session\"}";
        String unauthorized = "{\"errorCode\": \"forbidden\", \"message\": \"Not authorized to access specified resource\"}";

        return Publishers.just(HttpResponse.status(forbidden ? HttpStatus.FORBIDDEN :
                HttpStatus.UNAUTHORIZED).body(forbidden ? unauthorized : invalidSession));
    }
}
