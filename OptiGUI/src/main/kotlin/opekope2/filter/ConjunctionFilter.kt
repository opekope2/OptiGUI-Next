package opekope2.filter

/**
 * A filter, which applies the logical AND operation between the given filters and returns the result.
 * Only skips if all sub-filters skip, and only yields match if no sub-filters yield mismatch.
 *
 * This filter yields the first non-skipping mismatch result of all sub-filters if any,
 * or the first non-skipping match result of all sub-filters if any,
 * or a new skipping filter result (when all sub-filters skip)
 *
 * @param T The type the filter accepts
 * @param filters The sub-filters to evaluate
 */
class ConjunctionFilter<T>(private val filters: Iterable<Filter<T, out Any>>) : Filter<T, Unit>,
    Iterable<Filter<T, out Any>> {
    /**
     * Alternative constructor with variable arguments
     */
    constructor(vararg filters: Filter<T, out Any>) : this(filters.toList())

    override fun evaluate(value: T): FilterResult<Unit> = filters.map { it.evaluate(value) }.let { result ->
        if (result.any { it is FilterResult.Mismatch }) FilterResult.Mismatch()
        else if (result.all { it is FilterResult.Skip }) FilterResult.Skip()
        else FilterResult.Match(Unit)
    }

    override fun iterator(): Iterator<Filter<T, out Any>> = filters.iterator()

    override fun toString(): String = javaClass.name
}
