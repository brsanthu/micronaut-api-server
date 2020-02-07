package micronaut.api.server

import io.micronaut.security.authentication.*
import io.micronaut.security.authentication.AuthenticationProvider
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import javax.inject.Singleton

@Singleton
class AuthenticationProvider(): AuthenticationProvider {
  override fun authenticate(authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
    if (authenticationRequest != null && authenticationRequest.identity != null && authenticationRequest.secret != null) {
      if (authenticationRequest.identity == "admin" && authenticationRequest.secret == "password") {
        return Flowable.just<AuthenticationResponse>(UserDetails(authenticationRequest.identity as String, listOf("ROLE_ADMIN")))
      }
    }
    return Flowable.just<AuthenticationResponse>(AuthenticationFailed())
  }
}
