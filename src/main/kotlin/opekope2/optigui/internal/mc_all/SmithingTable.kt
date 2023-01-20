package opekope2.optigui.internal.mc_all

import net.minecraft.util.math.BlockPos
import opekope2.filter.*
import opekope2.optigui.interaction.Interaction
import opekope2.optigui.internal.properties.GeneralProperties
import opekope2.optigui.provider.IRegistryLookupProvider
import opekope2.optigui.provider.getProvider
import opekope2.optigui.resource.Resource
import opekope2.util.TexturePath
import opekope2.util.resolvePath
import opekope2.util.resolveResource
import java.io.File

private const val container = "_smithing_table"
private val texture = TexturePath.SMITHING_TABLE

fun createSmithingTableFilter(resource: Resource): FilterInfo? {
    if (resource.properties["container"] != container) return null
    val resFolder = File(resource.id.path).parent.replace('\\', '/')
    val replacement = (resource.properties["texture"] as? String)?.let {
        resource.resourceManager.resolveResource(resolvePath(resFolder, it))
    } ?: return null

    val filters = ConjunctionFilter(createGeneralFilters(resource, container, texture))

    return FilterInfo(
        PostProcessorFilter(
            Filter {
                filters.evaluate(processSmithingTableInteraction(it) ?: return@Filter FilterResult.Mismatch())
            },
            replacement
        ),
        setOf(texture)
    )
}

private typealias SmithingTableProperties = GeneralProperties

private fun processSmithingTableInteraction(interaction: Interaction): Interaction? {
    val lookup = getProvider<IRegistryLookupProvider>()

    val world = interaction.rawInteraction?.world ?: return null
    val pos = BlockPos(interaction.rawInteraction.hitResult?.pos ?: return null)

    return interaction.copy(
        data = SmithingTableProperties(
            container = container,
            texture = texture,
            name = null,
            biome = lookup.lookupBiome(world, pos),
            height = pos.y
        )
    )
}