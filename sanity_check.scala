object PaDDLeDSL {
  import PaDDLe.*

  val __ = Wildcard

  extension (name: String) {
    def & = Var(name)
    def * = Val(name)
    def l = Literal(name)
  }

  opaque type PartialBinding = Expression => Binding
  extension (f: PartialBinding) {
    def ~=(expr: Expression) = f(expr)
  }

  def let(name: String)(parameters: SubPattern*): PartialBinding = Binding(name, parameters, _)
  def ??(name: String)(parameters: SubPattern*) = Mention(name, parameters)
  def !!(name: String)(arguments: Expression*) = Match(name, arguments)

  def patterns(ps: Binding*) = ps
}

@main def runSanityCheck = {
  import PaDDLe.{evaluate as eval}
  import PaDDLeDSL.*

  val `identity pattern` = patterns(
    let("Identity")("x".&) ~= "x".*
  )

  assert(eval(`identity pattern`, !!("Identity")("hello".l)) == "hello".l)
  assert(eval(`identity pattern`, !!("Identity")("42".l)) == "42".l)

  val `or pattern` = patterns(
    let("True")() ~= "true".l,
    let("False")() ~= "false".l,
    let("Or")(??("True")(), __) ~= !!("True")(),
    let("Or")(__, "x".&) ~= "x".*,
  )

  assert(eval(`or pattern`, !!("True")()) == "true".l)
  assert(eval(`or pattern`, !!("False")()) == "false".l)
  assert(eval(`or pattern`, !!("Or")(!!("True")(), !!("False")())) == "true".l)

  val `list pattern` = patterns(
    let("Nil")() ~= "nil".l,
    let("Cons")("x".&, "xs".&) ~= "cons".l,
    let("Car")(??("Cons")("x".&, __)) ~= "x".*,
    let("Cdr")(??("Cons")(__, "xs".&)) ~= "xs".*,
  )

  assert(eval(`list pattern`, !!("Nil")()) == "nil".l)
  assert(eval(`list pattern`, !!("Cons")("hello".l, "world".l)) == "hello".l)
  assert(eval(`list pattern`, !!("Car")(!!("Cons")("hello".l, "world".l))) == "hello".l)
  assert(eval(`list pattern`, !!("Cdr")(!!("Cons")("hello".l, "world".l))) == "world".l)
  assert(eval(`list pattern`, !!("Car")(!!("Cdr")(!!("Cons")("hello".l, !!("Cons")("kind".l, "world".l))))) == "kind".l)
  assert(eval(`list pattern`, !!("Cdr")(!!("Cdr")(!!("Cons")("hello".l, !!("Cons")("kind".l, "world".l))))) == "world".l)
}
