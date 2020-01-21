package test.org.springdoc.api.app2

import kotlinx.coroutines.reactor.mono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

enum class SystemStatus(val status: String) {
    OK("OK")
}

data class SystemStatusResponse(
        val status: SystemStatus
)

@RestController
@RequestMapping("/status")
class SystemStatusController {
    @GetMapping
    suspend fun index() = SystemStatusResponse(SystemStatus.OK)

    @GetMapping("/foo")
    fun foo() = mono {
        SystemStatusResponse(SystemStatus.OK)
    }
}
