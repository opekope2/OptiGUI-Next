package opekope2.optigui.internal.mc_all

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BrewingStandBlockEntity
import net.minecraft.util.Nameable
import opekope2.filter.ConjunctionFilter
import opekope2.filter.FilterInfo
import opekope2.filter.PostProcessorFilter
import opekope2.optigui.internal.properties.OptiFineProperties
import opekope2.optigui.provider.RegistryLookup
import opekope2.optigui.provider.getProvider
import opekope2.optigui.resource.Resource
import opekope2.util.TexturePath

private const val CONTAINER = "brewing_stand"
private val texture = TexturePath.BREWING_STAND

fun createBrewingStandFilter(resource: Resource): FilterInfo? {
    if (resource.properties["container"] != CONTAINER) return null
    val replacement = findReplacementTexture(resource) ?: return null

    val filters = createGeneralFilters(resource, CONTAINER, texture)

    return FilterInfo(
        PostProcessorFilter(ConjunctionFilter(filters), replacement),
        setOf(texture)
    )
}

private typealias BrewingStandProperties = OptiFineProperties

internal fun processBrewingStand(brewingStand: BlockEntity): Any? {
    if (brewingStand !is BrewingStandBlockEntity) return null
    val lookup = getProvider<RegistryLookup>()

    val world = brewingStand.world ?: return null

    return BrewingStandProperties(
        container = CONTAINER,
        texture = texture,
        name = (brewingStand as? Nameable)?.customName?.string,
        biome = lookup.lookupBiome(world, brewingStand.pos),
        height = brewingStand.pos.y
    )
}