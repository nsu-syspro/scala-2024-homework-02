/** PaDDLe (PAttern Definition & Detection LanguagE) pattern matching engine.
  * 
  * You should finish implementation of this module according
  * to the reference described in `README.md` to complete this assignment.
  */
object PaDDLe {
  /** Description of data structure that may be matched and destructured with PaDDLe. */
  sealed trait Pattern

  /** Binding of given `name` and set of `parameters` with some `value`, in which both `name` and `parameters` may reoccur. */
  case class Binding(name: String, parameters: Seq[SubPattern], value: Expression) extends Pattern

  /** Pattern that may occur only as a part of other pattern. */
  sealed trait SubPattern extends Pattern

  /** Parameter which should be associated with some data during matching.
    * Example:
    * Let's declare following pattern:
    *  `Binding(name = "Identity", parameters = Seq(Var("x")), value = Val("x"))`
    * 
    * If we will try to match this pattern against following expression:
    *  `Match("Identity", parameters = Seq(Literal("hello")))`
    * 
    * expression `Literal("hello")` should be obtained after evaluation.
    */
  case class Var(name: String) extends SubPattern

  /** Parameter that shouldn't be associated with any data.
    * 
    * Example:
    *  In pattern `Binding(name = "Sink", parameters = Seq(Wildcard), value = Literal("no results"))`, no data should be bound to `Wildcard` in any scenario of matching:
    * `Match("Sink", parameters = Seq(Literal("hello")))`
    * should be evaluated to `Literal("no results")`.
    */
  case object Wildcard extends SubPattern

  /** "Reference" to [[Binding]] with given `name` and set of `parameters` which may occur
    as a part of some pattern.
    */
  case class Mention(name: String, parameters: Seq[SubPattern]) extends SubPattern


  /** Expression which may raise matching the pattern against
    * actual value or be evaluated as literal.
    */
  sealed trait Expression
  
  /** Just a literal value. */
  case class Literal(name: String) extends Expression
  
  /** Match of some pattern defined againts it's `name` and provided `arguments`. */
  case class Match(name: String, arguments: Seq[Expression]) extends Expression

  /** Actual value available by `name` and bound to the parameters of in current matching context. */
  case class Val(name: String) extends Expression


  /** Helper structure representing current matching context.
    *
    * '''Feel free to modify this definition in your code.'''
    */
  private type LocalEnvironment = Map[Var, Expression]


  /** Declares given `patterns` and evaluates `expression`:
    * performs required matching against declared `patterns`
    * moving up-to-down and choosing the first suitable
    * variant.
    */
  def evaluate(patterns: Seq[Pattern], expression: Expression): Expression = ???
}
