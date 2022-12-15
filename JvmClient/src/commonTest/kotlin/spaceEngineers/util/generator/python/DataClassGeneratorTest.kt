package spaceEngineers.util.generator.python

import spaceEngineers.graph.DataNode
import spaceEngineers.model.BlockId
import spaceEngineers.model.DefinitionId
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals

class DataClassGeneratorTest {

    data class MapListTest(val l: List<String>, val m: Map<Int, Short>)

    // TODO: test on DataNode

    @Test
    fun `test generateDataClass method with DefinitionId class`() {
        val ktype = DefinitionId::class.createType()

        assertEquals(
            "@dataclass\nclass DefinitionId:\n    Id: 'str'\n    Type: 'str'", generateDataClass(ktype, emptySet())
        )
    }

    @Test
    fun `test toPythonType method with basic type`() {
        val ktype = typeOf<String>()
        assertEquals("str", ktype.toPythonType(emptySet()))
    }

    @Test
    fun `test toPythonType method with typealias`() {
        val ktype = typeOf<BlockId>()
        assertEquals("str", ktype.toPythonType(emptySet()))
    }

    @Test
    fun `test generateDataClass and toPythonType methods with DefinitionId class`() {
        // Create KType instances for the DefinitionId class and its properties
        val ktype = DefinitionId::class.createType()
        val idPropType = String::class.createType()
        val typePropType = String::class.createType()

        // Generate the Python data class for the DefinitionId class
        val generatedClass = generateDataClass(ktype, emptySet())
        assertEquals("@dataclass\nclass DefinitionId:\n    Id: 'str'\n    Type: 'str'", generatedClass)

        // Convert the property types to their Python equivalents
        assertEquals("str", idPropType.toPythonType(emptySet()))
        assertEquals("str", typePropType.toPythonType(emptySet()))
    }

    @Test
    fun `test generateDataClass method with MapListTest class`() {
        // Create KType instances for the MapListTest class and its properties
        val ktype = MapListTest::class.createType()

        val generatedClass = generateDataClass(ktype, emptySet())
        assertEquals("@dataclass\nclass MapListTest:\n    L: 'List[str]'\n    M: 'Dict[int, int]'", generatedClass)
    }

    @Test
    fun `test generateDataClass method with MapListTest class2`() {

        val lPropType = typeOf<List<String>>()
        val mPropType = typeOf<Map<Int, Int>>()

        // Convert the property types to their Python equivalents
        assertEquals("List[str]", lPropType.toPythonType(emptySet()))
        assertEquals("Dict[int, int]", mPropType.toPythonType(emptySet()))
    }

    @Test
    fun `test generateDataClass method with DataNode class2`() {

        val lPropType = typeOf<DataNode<Int, String>>()

        assertEquals(
            """NodeId = TypeVar('NodeId')
NodeData = TypeVar('NodeData')
@dataclass
class DataNode(Generic[NodeId, NodeData]):
    Data: 'NodeData'
    Id: 'NodeId'""",
            generateDataClass(lPropType, emptySet())
        )
    }

    @Test
    fun `test toPythonType generics`() {

        val lPropType = List::class.createType(listOf(KTypeProjection(KVariance.INVARIANT, String::class.createType())))

        assertEquals("List[str]", lPropType.toPythonType(emptySet()))
    }
}
