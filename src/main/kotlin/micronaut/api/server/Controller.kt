package micronaut.api.server

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.utils.SecurityService

@Controller("/")
@Secured(SecurityRule.IS_AUTHENTICATED)
class Controller(val securityService: SecurityService) {
  @Get("/status")
  @Secured(SecurityRule.IS_ANONYMOUS)
  fun getStatus(): String {
    return "User is: ${securityService.username()}"
  }

  @Get("/profile")
  fun getProfile(): String {
    return "User is: ${securityService.username()}"
  }
}
