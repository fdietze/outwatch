// package nomad

import cats._
import cats.effect._
import simulacrum._

package object nomad {
  @typeclass
  trait Cancellable[C] {
    def cancel[F[_]:Effect](c:C):F[Unit]
  }

  object Source {
    def apply[M[_], C](implicit F: Source[M,C]) = F
  }
  trait Source[M[_], C] extends Applicative[M] {

    def subscribe[T, F[_]:Effect](s : M[T])(onNext: T => F[Unit])(implicit C: Cancellable[C]): F[C]
  }

  @typeclass
  trait Sink[M[_]] {
    def next[T,F[_]](s : M[T])(value: T): F[Unit]
  }
}
