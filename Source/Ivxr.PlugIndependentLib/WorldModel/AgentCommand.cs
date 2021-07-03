using System.Diagnostics.CodeAnalysis;

namespace Iv4xr.PluginLib.WorldModel
{
    // This enum follows Java / Kotlin naming convention.
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public enum ObservationMode
    {
        BASIC,
        BLOCKS,
        NEW_BLOCKS,
    }
}
