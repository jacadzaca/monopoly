import com.jacadzaca.monopoly.MainVerticle
import io.vertx.reactivex.core.Vertx

fun main() {
  Vertx.vertx().deployVerticle(MainVerticle())
}
