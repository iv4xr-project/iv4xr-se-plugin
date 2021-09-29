(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin', 'kotlinx-serialization-kotlinx-serialization-core-js-legacy'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'), require('kotlinx-serialization-kotlinx-serialization-core-js-legacy'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'kotlinx-serialization-kotlinx-serialization-json-js-legacy'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'kotlinx-serialization-kotlinx-serialization-json-js-legacy'.");
    }if (typeof this['kotlinx-serialization-kotlinx-serialization-core-js-legacy'] === 'undefined') {
      throw new Error("Error loading module 'kotlinx-serialization-kotlinx-serialization-json-js-legacy'. Its dependency 'kotlinx-serialization-kotlinx-serialization-core-js-legacy' was not found. Please, check whether 'kotlinx-serialization-kotlinx-serialization-core-js-legacy' is loaded prior to 'kotlinx-serialization-kotlinx-serialization-json-js-legacy'.");
    }root['kotlinx-serialization-kotlinx-serialization-json-js-legacy'] = factory(typeof this['kotlinx-serialization-kotlinx-serialization-json-js-legacy'] === 'undefined' ? {} : this['kotlinx-serialization-kotlinx-serialization-json-js-legacy'], kotlin, this['kotlinx-serialization-kotlinx-serialization-core-js-legacy']);
  }
}(this, function (_, Kotlin, $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy) {
  'use strict';
  var $$importsForInline$$ = _.$$importsForInline$$ || (_.$$importsForInline$$ = {});
  var Kind_CLASS = Kotlin.Kind.CLASS;
  var UInt_init = Kotlin.kotlin.UInt;
  var ULong_init = Kotlin.kotlin.ULong;
  var UByte_init = Kotlin.kotlin.UByte;
  var UShort_init = Kotlin.kotlin.UShort;
  var SerializationException_init = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.SerializationException_init_pdl1vj$;
  var SerializationException = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.SerializationException;
  var coerceAtLeast = Kotlin.kotlin.ranges.coerceAtLeast_dqglrj$;
  var coerceAtMost = Kotlin.kotlin.ranges.coerceAtMost_dqglrj$;
  var toChar = Kotlin.toChar;
  var kotlin_js_internal_ByteCompanionObject = Kotlin.kotlin.js.internal.ByteCompanionObject;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var unboxChar = Kotlin.unboxChar;
  var equals = Kotlin.equals;
  var indexOf = Kotlin.kotlin.text.indexOf_8eortd$;
  var ensureNotNull = Kotlin.ensureNotNull;
  var CharRange = Kotlin.kotlin.ranges.CharRange;
  var last = Kotlin.kotlin.collections.last_2p1efm$;
  var removeLast = Kotlin.kotlin.collections.removeLast_vvxzk3$;
  var lastIndexOf = Kotlin.kotlin.text.lastIndexOf_l5u8uk$;
  var defineInlineFunction = Kotlin.defineInlineFunction;
  var L0 = Kotlin.Long.ZERO;
  var Long$Companion$MIN_VALUE = Kotlin.Long.MIN_VALUE;
  var get_indices = Kotlin.kotlin.text.get_indices_gw00vp$;
  var StringBuilder_init = Kotlin.kotlin.text.StringBuilder_init;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var getValue = Kotlin.kotlin.collections.getValue_t9ocha$;
  var singleOrNull = Kotlin.kotlin.collections.singleOrNull_2p1efm$;
  var emptyMap = Kotlin.kotlin.collections.emptyMap_q3lmfv$;
  var getCallableRef = Kotlin.getCallableRef;
  var Map = Kotlin.kotlin.collections.Map;
  var throwCCE = Kotlin.throwCCE;
  var LinkedHashMap_init = Kotlin.kotlin.collections.LinkedHashMap_init_q3lmfv$;
  var AbstractPolymorphicSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.internal.AbstractPolymorphicSerializer;
  var Any = Object;
  var wrapFunction = Kotlin.wrapFunction;
  var findPolymorphicSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.findPolymorphicSerializer_dz33bw$;
  var SealedClassSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.SealedClassSerializer;
  var jsonCachedSerialNames = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.internal.jsonCachedSerialNames_583jlf$;
  var SerialKind = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.SerialKind;
  var PrimitiveKind = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.PrimitiveKind;
  var PolymorphicKind = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.PolymorphicKind;
  var getKClass = Kotlin.getKClass;
  var DeserializationStrategy = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.DeserializationStrategy;
  var toString = Kotlin.toString;
  var IllegalStateException_init = Kotlin.kotlin.IllegalStateException_init_pdl1vj$;
  var IllegalArgumentException_init = Kotlin.kotlin.IllegalArgumentException_init_pdl1vj$;
  var StructureKind = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.StructureKind;
  var SerializersModuleCollector = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.modules.SerializersModuleCollector;
  var toByte = Kotlin.toByte;
  var toShort = Kotlin.toShort;
  var isFinite = Kotlin.kotlin.isFinite_81szk$;
  var toDouble = Kotlin.kotlin.text.toDouble_pdl1vz$;
  var isFinite_0 = Kotlin.kotlin.isFinite_yrwdxr$;
  var toBoxedChar = Kotlin.toBoxedChar;
  var AbstractDecoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.AbstractDecoder;
  var toUInt = Kotlin.kotlin.text.toUInt_pdl1vz$;
  var toULong = Kotlin.kotlin.text.toULong_pdl1vz$;
  var toUByte = Kotlin.kotlin.text.toUByte_pdl1vz$;
  var toUShort = Kotlin.kotlin.text.toUShort_pdl1vz$;
  var IllegalArgumentException = Kotlin.kotlin.IllegalArgumentException;
  var serializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.serializer_wxu9yi$;
  var serializer_0 = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.serializer_de2ylp$;
  var serializer_1 = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.serializer_28ajz5$;
  var serializer_2 = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.serializer_wdte4j$;
  var setOf = Kotlin.kotlin.collections.setOf_i5x0yv$;
  var AbstractEncoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.AbstractEncoder;
  var SerializationStrategy = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.SerializationStrategy;
  var equals_0 = Kotlin.kotlin.text.equals_igcy3c$;
  var IllegalArgumentException_init_0 = Kotlin.kotlin.IllegalArgumentException_init;
  var IntRange = Kotlin.kotlin.ranges.IntRange;
  var kotlin_js_internal_ShortCompanionObject = Kotlin.kotlin.js.internal.ShortCompanionObject;
  var single = Kotlin.kotlin.text.single_gw00vp$;
  var NamedValueDecoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.internal.NamedValueDecoder;
  var plus = Kotlin.kotlin.collections.plus_khz7k3$;
  var toList = Kotlin.kotlin.collections.toList_7wnvza$;
  var toInt = Kotlin.kotlin.text.toInt_pdl1vz$;
  var emptySet = Kotlin.kotlin.collections.emptySet_287e2$;
  var Unit = Kotlin.kotlin.Unit;
  var throwUPAE = Kotlin.throwUPAE;
  var NamedValueEncoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.internal.NamedValueEncoder;
  var Enum = Kotlin.kotlin.Enum;
  var throwISE = Kotlin.throwISE;
  var modules = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.modules;
  var StringFormat = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.StringFormat;
  var iterator = Kotlin.kotlin.text.iterator_gw00vp$;
  var serializerOrNull = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.serializerOrNull_1yb8b7$;
  var KSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.KSerializer;
  var buildSerialDescriptor = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.buildSerialDescriptor_2yu4m$;
  var Kind_INTERFACE = Kotlin.Kind.INTERFACE;
  var Decoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.Decoder;
  var CompositeDecoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.CompositeDecoder;
  var hashCode = Kotlin.hashCode;
  var joinToString = Kotlin.kotlin.collections.joinToString_fmv235$;
  var List = Kotlin.kotlin.collections.List;
  var toIntOrNull = Kotlin.kotlin.text.toIntOrNull_pdl1vz$;
  var toLong = Kotlin.kotlin.text.toLong_pdl1vz$;
  var toLongOrNull = Kotlin.kotlin.text.toLongOrNull_pdl1vz$;
  var toDoubleOrNull = Kotlin.kotlin.text.toDoubleOrNull_pdl1vz$;
  var Annotation = Kotlin.kotlin.Annotation;
  var toULongOrNull = Kotlin.kotlin.text.toULongOrNull_pdl1vz$;
  var PrimitiveSerialDescriptor = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.PrimitiveSerialDescriptor_xytc2a$;
  var HashMap = Kotlin.kotlin.collections.HashMap;
  var PrimitiveClasses$stringClass = Kotlin.kotlin.reflect.js.internal.PrimitiveClasses.stringClass;
  var createKType = Kotlin.createKType;
  var createInvariantKTypeProjection = Kotlin.createInvariantKTypeProjection;
  var SerialDescriptor = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.descriptors.SerialDescriptor;
  var kotlin_js_internal_StringCompanionObject = Kotlin.kotlin.js.internal.StringCompanionObject;
  var serializer_3 = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.serializer_6eet4j$;
  var MapSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.MapSerializer_2yqygg$;
  var ListSerializer = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.builtins.ListSerializer_swdriu$;
  var lazy = Kotlin.kotlin.lazy_klfg04$;
  var serializer_4 = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.serializer_saj79j$;
  var Encoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.Encoder;
  var CompositeEncoder = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy.kotlinx.serialization.encoding.CompositeEncoder;
  var HashMap_init = Kotlin.kotlin.collections.HashMap_init_bwtc7$;
  var L9007199254740991 = new Kotlin.Long(-1, 2097151);
  var numberToChar = Kotlin.numberToChar;
  var toByteOrNull = Kotlin.kotlin.text.toByteOrNull_pdl1vz$;
  var toShortOrNull = Kotlin.kotlin.text.toShortOrNull_pdl1vz$;
  var toBoolean = Kotlin.kotlin.text.toBoolean_5cw0du$;
  var JsMath = Math;
  var abs = Kotlin.kotlin.math.abs_s8cxhz$;
  var StringBuilder_init_0 = Kotlin.kotlin.text.StringBuilder_init_za3lpa$;
  ComposerForUnsignedNumbers.prototype = Object.create(Composer.prototype);
  ComposerForUnsignedNumbers.prototype.constructor = ComposerForUnsignedNumbers;
  JsonException.prototype = Object.create(SerializationException.prototype);
  JsonException.prototype.constructor = JsonException;
  JsonDecodingException.prototype = Object.create(JsonException.prototype);
  JsonDecodingException.prototype.constructor = JsonDecodingException;
  JsonEncodingException.prototype = Object.create(JsonException.prototype);
  JsonEncodingException.prototype.constructor = JsonEncodingException;
  StreamingJsonDecoder.prototype = Object.create(AbstractDecoder.prototype);
  StreamingJsonDecoder.prototype.constructor = StreamingJsonDecoder;
  JsonDecoderForUnsignedTypes.prototype = Object.create(AbstractDecoder.prototype);
  JsonDecoderForUnsignedTypes.prototype.constructor = JsonDecoderForUnsignedTypes;
  StreamingJsonEncoder.prototype = Object.create(AbstractEncoder.prototype);
  StreamingJsonEncoder.prototype.constructor = StreamingJsonEncoder;
  AbstractJsonTreeDecoder.prototype = Object.create(NamedValueDecoder.prototype);
  AbstractJsonTreeDecoder.prototype.constructor = AbstractJsonTreeDecoder;
  JsonPrimitiveDecoder.prototype = Object.create(AbstractJsonTreeDecoder.prototype);
  JsonPrimitiveDecoder.prototype.constructor = JsonPrimitiveDecoder;
  JsonTreeDecoder.prototype = Object.create(AbstractJsonTreeDecoder.prototype);
  JsonTreeDecoder.prototype.constructor = JsonTreeDecoder;
  JsonTreeMapDecoder.prototype = Object.create(JsonTreeDecoder.prototype);
  JsonTreeMapDecoder.prototype.constructor = JsonTreeMapDecoder;
  JsonTreeListDecoder.prototype = Object.create(AbstractJsonTreeDecoder.prototype);
  JsonTreeListDecoder.prototype.constructor = JsonTreeListDecoder;
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype = Object.create(AbstractEncoder.prototype);
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.constructor = AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral;
  AbstractJsonTreeEncoder.prototype = Object.create(NamedValueEncoder.prototype);
  AbstractJsonTreeEncoder.prototype.constructor = AbstractJsonTreeEncoder;
  JsonPrimitiveEncoder.prototype = Object.create(AbstractJsonTreeEncoder.prototype);
  JsonPrimitiveEncoder.prototype.constructor = JsonPrimitiveEncoder;
  JsonTreeEncoder.prototype = Object.create(AbstractJsonTreeEncoder.prototype);
  JsonTreeEncoder.prototype.constructor = JsonTreeEncoder;
  JsonTreeMapEncoder.prototype = Object.create(JsonTreeEncoder.prototype);
  JsonTreeMapEncoder.prototype.constructor = JsonTreeMapEncoder;
  JsonTreeListEncoder.prototype = Object.create(AbstractJsonTreeEncoder.prototype);
  JsonTreeListEncoder.prototype.constructor = JsonTreeListEncoder;
  WriteMode.prototype = Object.create(Enum.prototype);
  WriteMode.prototype.constructor = WriteMode;
  Json$Default.prototype = Object.create(Json.prototype);
  Json$Default.prototype.constructor = Json$Default;
  JsonImpl.prototype = Object.create(Json.prototype);
  JsonImpl.prototype.constructor = JsonImpl;
  JsonPrimitive.prototype = Object.create(JsonElement.prototype);
  JsonPrimitive.prototype.constructor = JsonPrimitive;
  JsonLiteral.prototype = Object.create(JsonPrimitive.prototype);
  JsonLiteral.prototype.constructor = JsonLiteral;
  JsonNull.prototype = Object.create(JsonPrimitive.prototype);
  JsonNull.prototype.constructor = JsonNull;
  JsonObject.prototype = Object.create(JsonElement.prototype);
  JsonObject.prototype.constructor = JsonObject;
  JsonArray.prototype = Object.create(JsonElement.prototype);
  JsonArray.prototype.constructor = JsonArray;
  DynamicInput.prototype = Object.create(NamedValueDecoder.prototype);
  DynamicInput.prototype.constructor = DynamicInput;
  DynamicMapInput.prototype = Object.create(DynamicInput.prototype);
  DynamicMapInput.prototype.constructor = DynamicMapInput;
  DynamicListInput.prototype = Object.create(DynamicInput.prototype);
  DynamicListInput.prototype.constructor = DynamicListInput;
  PrimitiveDynamicInput.prototype = Object.create(DynamicInput.prototype);
  PrimitiveDynamicInput.prototype.constructor = PrimitiveDynamicInput;
  DynamicObjectEncoder$WriteMode.prototype = Object.create(Enum.prototype);
  DynamicObjectEncoder$WriteMode.prototype.constructor = DynamicObjectEncoder$WriteMode;
  DynamicObjectEncoder.prototype = Object.create(AbstractEncoder.prototype);
  DynamicObjectEncoder.prototype.constructor = DynamicObjectEncoder;
  DynamicPrimitiveEncoder.prototype = Object.create(AbstractEncoder.prototype);
  DynamicPrimitiveEncoder.prototype.constructor = DynamicPrimitiveEncoder;
  function Composer(sb, json) {
    this.sb_8be2vx$ = sb;
    this.json_8be2vx$ = json;
    this.level_0 = 0;
    this.writingFirst_rjxylk$_0 = true;
  }
  Object.defineProperty(Composer.prototype, 'writingFirst', {
    configurable: true,
    get: function () {
      return this.writingFirst_rjxylk$_0;
    },
    set: function (writingFirst) {
      this.writingFirst_rjxylk$_0 = writingFirst;
    }
  });
  Composer.prototype.indent = function () {
    this.writingFirst = true;
    this.level_0 = this.level_0 + 1 | 0;
  };
  Composer.prototype.unIndent = function () {
    this.level_0 = this.level_0 - 1 | 0;
  };
  Composer.prototype.nextItem = function () {
    this.writingFirst = false;
    if (this.json_8be2vx$.configuration.prettyPrint) {
      this.print_61zpoe$('\n');
      var times = this.level_0;
      for (var index = 0; index < times; index++) {
        this.print_61zpoe$(this.json_8be2vx$.configuration.prettyPrintIndent);
      }
    }};
  Composer.prototype.space = function () {
    if (this.json_8be2vx$.configuration.prettyPrint)
      this.print_s8itvh$(32);
  };
  Composer.prototype.print_s8itvh$ = function (v) {
    this.sb_8be2vx$.append_s8itvh$(v);
  };
  Composer.prototype.print_61zpoe$ = function (v) {
    this.sb_8be2vx$.append_61zpoe$(v);
  };
  Composer.prototype.print_mx4ult$ = function (v) {
    this.sb_8be2vx$.append_61zpoe$(v.toString());
  };
  Composer.prototype.print_14dthe$ = function (v) {
    this.sb_8be2vx$.append_61zpoe$(v.toString());
  };
  Composer.prototype.print_s8j3t7$ = function (v) {
    this.sb_8be2vx$.append_s8cxhz$(Kotlin.Long.fromInt(v));
  };
  Composer.prototype.print_mq22fl$ = function (v) {
    this.sb_8be2vx$.append_s8cxhz$(Kotlin.Long.fromInt(v));
  };
  Composer.prototype.print_za3lpa$ = function (v) {
    this.sb_8be2vx$.append_s8cxhz$(Kotlin.Long.fromInt(v));
  };
  Composer.prototype.print_s8cxhz$ = function (v) {
    this.sb_8be2vx$.append_s8cxhz$(v);
  };
  Composer.prototype.print_6taknv$ = function (v) {
    this.sb_8be2vx$.append_61zpoe$(v.toString());
  };
  Composer.prototype.printQuoted_61zpoe$ = function (value) {
    this.sb_8be2vx$.appendQuoted_61zpoe$(value);
  };
  Composer.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Composer',
    interfaces: []
  };
  function ComposerForUnsignedNumbers(sb, json) {
    Composer.call(this, sb, json);
  }
  ComposerForUnsignedNumbers.prototype.print_za3lpa$ = function (v) {
    return Composer.prototype.print_61zpoe$.call(this, (new UInt_init(v)).toString());
  };
  ComposerForUnsignedNumbers.prototype.print_s8cxhz$ = function (v) {
    return Composer.prototype.print_61zpoe$.call(this, (new ULong_init(v)).toString());
  };
  ComposerForUnsignedNumbers.prototype.print_s8j3t7$ = function (v) {
    return Composer.prototype.print_61zpoe$.call(this, (new UByte_init(v)).toString());
  };
  ComposerForUnsignedNumbers.prototype.print_mq22fl$ = function (v) {
    return Composer.prototype.print_61zpoe$.call(this, (new UShort_init(v)).toString());
  };
  ComposerForUnsignedNumbers.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'ComposerForUnsignedNumbers',
    interfaces: [Composer]
  };
  function JsonException(message) {
    SerializationException_init(message, this);
    this.name = 'JsonException';
  }
  JsonException.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonException',
    interfaces: [SerializationException]
  };
  function JsonDecodingException(message) {
    JsonException.call(this, message);
    this.name = 'JsonDecodingException';
  }
  JsonDecodingException.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonDecodingException',
    interfaces: [JsonException]
  };
  function JsonDecodingException_0(offset, message) {
    return new JsonDecodingException(offset >= 0 ? 'Unexpected JSON token at offset ' + offset + ': ' + message : message);
  }
  function JsonEncodingException(message) {
    JsonException.call(this, message);
    this.name = 'JsonEncodingException';
  }
  JsonEncodingException.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonEncodingException',
    interfaces: [JsonException]
  };
  function JsonDecodingException_1(offset, message, input) {
    return JsonDecodingException_0(offset, message + '\n' + 'JSON input: ' + minify(input, offset));
  }
  function InvalidFloatingPointEncoded(value, output) {
    return new JsonEncodingException('Unexpected special floating-point value ' + value.toString() + '. By default, ' + 'non-finite floating point values are prohibited because they do not conform JSON specification. ' + "It is possible to deserialize them using 'JsonBuilder.allowSpecialFloatingPointValues = true'\n" + ('Current output: ' + minify(output)));
  }
  function InvalidFloatingPointEncoded_0(value, key, output) {
    return new JsonEncodingException(unexpectedFpErrorMessage(value, key, output));
  }
  function InvalidFloatingPointDecoded(value, key, output) {
    return JsonDecodingException_0(-1, unexpectedFpErrorMessage(value, key, output));
  }
  function throwInvalidFloatingPointDecoded($receiver, result) {
    $receiver.fail_bm4lxs$('Unexpected special floating-point value ' + result.toString() + '. By default, ' + 'non-finite floating point values are prohibited because they do not conform JSON specification. ' + specialFlowingValuesHint);
  }
  function unexpectedFpErrorMessage(value, key, output) {
    return 'Unexpected special floating-point value ' + value.toString() + ' with key ' + key + '. By default, ' + 'non-finite floating point values are prohibited because they do not conform JSON specification. ' + "It is possible to deserialize them using 'JsonBuilder.allowSpecialFloatingPointValues = true'\n" + ('Current output: ' + minify(output));
  }
  function UnknownKeyException(key, input) {
    return JsonDecodingException_0(-1, "Encountered unknown key '" + key + "'." + '\n' + "Use 'ignoreUnknownKeys = true' in 'Json {}' builder to ignore unknown keys.\n" + ('Current input: ' + minify(input)));
  }
  function InvalidKeyKindException(keyDescriptor) {
    return new JsonEncodingException("Value of type '" + keyDescriptor.serialName + "' can't be used in JSON as a key in the map. " + ("It should have either primitive or enum kind, but its kind is '" + keyDescriptor.kind + "'." + '\n') + allowStructuredMapKeysHint);
  }
  function minify($receiver, offset) {
    if (offset === void 0)
      offset = -1;
    if ($receiver.length < 200)
      return $receiver;
    if (offset === -1) {
      var start = $receiver.length - 60 | 0;
      if (start <= 0)
        return $receiver;
      return '.....' + $receiver.substring(start);
    }var start_0 = offset - 30 | 0;
    var end = offset + 30 | 0;
    var prefix = start_0 <= 0 ? '' : '.....';
    var suffix = end >= $receiver.length ? '' : '.....';
    var startIndex = coerceAtLeast(start_0, 0);
    var endIndex = coerceAtMost(end, $receiver.length);
    return prefix + $receiver.substring(startIndex, endIndex) + suffix;
  }
  var lenientHint;
  var coerceInputValuesHint;
  var specialFlowingValuesHint;
  var ignoreUnknownKeysHint;
  var allowStructuredMapKeysHint;
  var NULL;
  var COMMA;
  var COLON;
  var BEGIN_OBJ;
  var END_OBJ;
  var BEGIN_LIST;
  var END_LIST;
  var STRING;
  var STRING_ESC;
  var INVALID;
  var UNICODE_ESC;
  var TC_OTHER;
  var TC_STRING;
  var TC_STRING_ESC;
  var TC_WHITESPACE;
  var TC_COMMA;
  var TC_COLON;
  var TC_BEGIN_OBJ;
  var TC_END_OBJ;
  var TC_BEGIN_LIST;
  var TC_END_LIST;
  var TC_EOF;
  var TC_INVALID;
  var CTC_MAX;
  var ESC2C_MAX;
  var asciiCaseMask;
  function CharMappings() {
    CharMappings_instance = this;
    this.ESCAPE_2_CHAR = Kotlin.charArray(117);
    this.CHAR_TO_TOKEN = new Int8Array(126);
    this.initEscape_0();
    this.initCharToToken_0();
  }
  CharMappings.prototype.initEscape_0 = function () {
    for (var i = 0; i <= 31; i++) {
      this.initC2ESC_0(i, UNICODE_ESC);
    }
    this.initC2ESC_0(8, 98);
    this.initC2ESC_0(9, 116);
    this.initC2ESC_0(10, 110);
    this.initC2ESC_0(12, 102);
    this.initC2ESC_0(13, 114);
    this.initC2ESC_1(47, 47);
    this.initC2ESC_1(STRING, STRING);
    this.initC2ESC_1(STRING_ESC, STRING_ESC);
  };
  CharMappings.prototype.initCharToToken_0 = function () {
    for (var i = 0; i <= 32; i++) {
      this.initC2TC_0(i, TC_INVALID);
    }
    this.initC2TC_0(9, TC_WHITESPACE);
    this.initC2TC_0(10, TC_WHITESPACE);
    this.initC2TC_0(13, TC_WHITESPACE);
    this.initC2TC_0(32, TC_WHITESPACE);
    this.initC2TC_1(COMMA, TC_COMMA);
    this.initC2TC_1(COLON, TC_COLON);
    this.initC2TC_1(BEGIN_OBJ, TC_BEGIN_OBJ);
    this.initC2TC_1(END_OBJ, TC_END_OBJ);
    this.initC2TC_1(BEGIN_LIST, TC_BEGIN_LIST);
    this.initC2TC_1(END_LIST, TC_END_LIST);
    this.initC2TC_1(STRING, TC_STRING);
    this.initC2TC_1(STRING_ESC, TC_STRING_ESC);
  };
  CharMappings.prototype.initC2ESC_0 = function (c, esc) {
    if (esc !== UNICODE_ESC)
      this.ESCAPE_2_CHAR[esc | 0] = toChar(c);
  };
  CharMappings.prototype.initC2ESC_1 = function (c, esc) {
    this.initC2ESC_0(c | 0, esc);
  };
  CharMappings.prototype.initC2TC_0 = function (c, cl) {
    this.CHAR_TO_TOKEN[c] = cl;
  };
  CharMappings.prototype.initC2TC_1 = function (c, cl) {
    this.initC2TC_0(c | 0, cl);
  };
  CharMappings.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'CharMappings',
    interfaces: []
  };
  var CharMappings_instance = null;
  function CharMappings_getInstance() {
    if (CharMappings_instance === null) {
      new CharMappings();
    }return CharMappings_instance;
  }
  function charToTokenClass(c) {
    return (c | 0) < 126 ? CharMappings_getInstance().CHAR_TO_TOKEN[c | 0] : TC_OTHER;
  }
  function escapeToChar(c) {
    return unboxChar(c < 117 ? CharMappings_getInstance().ESCAPE_2_CHAR[c] : INVALID);
  }
  function JsonLexer(source) {
    this.source_0 = source;
    this.currentPosition = 0;
    this.peekedString_0 = null;
    this.escapedString_0 = StringBuilder_init();
  }
  JsonLexer.prototype.expectEof = function () {
    var nextToken = this.consumeNextToken();
    if (nextToken !== TC_EOF)
      this.fail_bm4lxs$('Expected EOF, but had ' + String.fromCharCode(this.source_0.charCodeAt(this.currentPosition - 1 | 0)) + ' instead');
  };
  JsonLexer.prototype.tryConsumeComma = function () {
    var current = this.skipWhitespaces_0();
    if (current === this.source_0.length)
      return false;
    if (this.source_0.charCodeAt(current) === 44) {
      this.currentPosition = this.currentPosition + 1 | 0;
      return true;
    }return false;
  };
  JsonLexer.prototype.canConsumeValue = function () {
    var current = this.currentPosition;
    while (current < this.source_0.length) {
      var c = this.source_0.charCodeAt(current);
      if (c === 32 || c === 10 || c === 13 || c === 9) {
        current = current + 1 | 0;
        continue;
      }this.currentPosition = current;
      return this.isValidValueStart_0(c);
    }
    this.currentPosition = current;
    return false;
  };
  JsonLexer.prototype.isValidValueStart_0 = function (c) {
    var tmp$;
    switch (c) {
      case 125:
      case 93:
      case 58:
      case 44:
        tmp$ = false;
        break;
      default:tmp$ = true;
        break;
    }
    return tmp$;
  };
  JsonLexer.prototype.consumeNextToken_s8j3t7$ = function (expected) {
    var token = this.consumeNextToken();
    if (token !== expected) {
      this.fail_0(expected);
    }return token;
  };
  JsonLexer.prototype.consumeNextToken_s8itvh$ = function (expected) {
    var tmp$;
    var source = this.source_0;
    while (this.currentPosition < source.length) {
      var c = source.charCodeAt((tmp$ = this.currentPosition, this.currentPosition = tmp$ + 1 | 0, tmp$));
      if (c === 32 || c === 10 || c === 13 || c === 9)
        continue;
      if (c === expected)
        return;
      this.unexpectedToken_0(expected);
    }
    this.unexpectedToken_0(expected);
  };
  JsonLexer.prototype.unexpectedToken_0 = function (expected) {
    this.currentPosition = this.currentPosition - 1 | 0;
    if (expected === STRING && equals(this.consumeStringLenient(), NULL)) {
      this.fail_bm4lxs$("Expected string literal but 'null' literal was found.\nUse 'coerceInputValues = true' in 'Json {}` builder to coerce nulls to default values.", this.currentPosition - 4 | 0);
    }this.fail_0(charToTokenClass(expected));
  };
  JsonLexer.prototype.fail_0 = function (expectedToken) {
    var tmp$;
    switch (expectedToken) {
      case 1:
        tmp$ = "quotation mark '\"'";
        break;
      case 4:
        tmp$ = "comma ','";
        break;
      case 5:
        tmp$ = "semicolon ':'";
        break;
      case 6:
        tmp$ = "start of the object '{'";
        break;
      case 7:
        tmp$ = "end of the object '}'";
        break;
      case 8:
        tmp$ = "start of the array '['";
        break;
      case 9:
        tmp$ = "end of the array ']'";
        break;
      default:tmp$ = 'valid token';
        break;
    }
    var expected = tmp$;
    var s = this.currentPosition === this.source_0.length || this.currentPosition <= 0 ? 'EOF' : String.fromCharCode(this.source_0.charCodeAt(this.currentPosition - 1 | 0));
    this.fail_bm4lxs$('Expected ' + expected + ", but had '" + s + "' instead", this.currentPosition - 1 | 0);
  };
  JsonLexer.prototype.peekNextToken = function () {
    var source = this.source_0;
    while (this.currentPosition < source.length) {
      var ch = source.charCodeAt(this.currentPosition);
      if (ch === 32 || ch === 10 || ch === 13 || ch === 9) {
        this.currentPosition = this.currentPosition + 1 | 0;
        continue;
      }return charToTokenClass(ch);
    }
    return TC_EOF;
  };
  JsonLexer.prototype.consumeNextToken = function () {
    var tmp$, tmp$_0;
    var source = this.source_0;
    while (this.currentPosition < source.length) {
      var ch = source.charCodeAt((tmp$ = this.currentPosition, this.currentPosition = tmp$ + 1 | 0, tmp$));
      var tc = charToTokenClass(ch);
      if (tc === TC_WHITESPACE)
        continue;
      else
        tmp$_0 = tc;
      return tmp$_0;
    }
    return TC_EOF;
  };
  JsonLexer.prototype.tryConsumeNotNull = function () {
    var current = this.skipWhitespaces_0();
    if ((this.source_0.length - current | 0) < 4)
      return true;
    for (var i = 0; i <= 3; i++) {
      if (NULL.charCodeAt(i) !== this.source_0.charCodeAt(current + i | 0))
        return true;
    }
    this.currentPosition = current + 4 | 0;
    return false;
  };
  JsonLexer.prototype.skipWhitespaces_0 = function () {
    var current = this.currentPosition;
    while (current < this.source_0.length) {
      var c = this.source_0.charCodeAt(current);
      if (c === 32 || c === 10 || c === 13 || c === 9) {
        current = current + 1 | 0;
      } else {
        break;
      }
    }
    this.currentPosition = current;
    return current;
  };
  JsonLexer.prototype.peekString_6taknv$ = function (isLenient) {
    var tmp$;
    var token = this.peekNextToken();
    if (isLenient) {
      if (token !== TC_STRING && token !== TC_OTHER)
        return null;
      tmp$ = this.consumeStringLenient();
    } else {
      if (token !== TC_STRING)
        return null;
      tmp$ = this.consumeString();
    }
    var string = tmp$;
    this.peekedString_0 = string;
    return string;
  };
  JsonLexer.prototype.consumeKeyString = function () {
    this.consumeNextToken_s8itvh$(STRING);
    var current = this.currentPosition;
    var closingQuote = indexOf(this.source_0, 34, current);
    if (closingQuote === -1)
      this.fail_0(TC_STRING);
    for (var i = current; i < closingQuote; i++) {
      if (this.source_0.charCodeAt(i) === STRING_ESC) {
        return this.consumeString_0(this.currentPosition, i);
      }}
    this.currentPosition = closingQuote + 1 | 0;
    return this.source_0.substring(current, closingQuote);
  };
  JsonLexer.prototype.consumeString = function () {
    if (this.peekedString_0 != null) {
      return this.takePeeked_0();
    }return this.consumeKeyString();
  };
  JsonLexer.prototype.consumeString_0 = function (startPosition, current) {
    var tmp$;
    var currentPosition = current;
    var lastPosition = startPosition;
    var source = this.source_0;
    var char = source.charCodeAt(currentPosition);
    while (char !== STRING) {
      if (char === STRING_ESC) {
        currentPosition = this.appendEscape_0(lastPosition, currentPosition);
        lastPosition = currentPosition;
      } else if ((currentPosition = currentPosition + 1 | 0, currentPosition) >= source.length) {
        this.fail_bm4lxs$('EOF', currentPosition);
      }char = source.charCodeAt(currentPosition);
    }
    if (lastPosition === startPosition) {
      var startIndex = lastPosition;
      var endIndex = currentPosition;
      tmp$ = source.substring(startIndex, endIndex);
    } else {
      tmp$ = this.decodedString_0(lastPosition, currentPosition);
    }
    var string = tmp$;
    this.currentPosition = currentPosition + 1 | 0;
    return string;
  };
  JsonLexer.prototype.appendEscape_0 = function (lastPosition, current) {
    this.escapedString_0.append_ezbsdh$(this.source_0, lastPosition, current);
    return this.appendEsc_0(current + 1 | 0);
  };
  JsonLexer.prototype.decodedString_0 = function (lastPosition, currentPosition) {
    this.appendRange_0(lastPosition, currentPosition);
    var result = this.escapedString_0.toString();
    this.escapedString_0.setLength_za3lpa$(0);
    return result;
  };
  JsonLexer.prototype.takePeeked_0 = function () {
    var $receiver = ensureNotNull(this.peekedString_0);
    this.peekedString_0 = null;
    return $receiver;
  };
  JsonLexer.prototype.consumeStringLenient = function () {
    if (this.peekedString_0 != null) {
      return this.takePeeked_0();
    }var current = this.skipWhitespaces_0();
    if (current >= this.source_0.length)
      this.fail_bm4lxs$('EOF', current);
    var token = charToTokenClass(this.source_0.charCodeAt(current));
    if (token === TC_STRING) {
      return this.consumeString();
    }if (token !== TC_OTHER) {
      this.fail_bm4lxs$('Expected beginning of the string, but got ' + String.fromCharCode(this.source_0.charCodeAt(current)));
    }while (current < this.source_0.length && charToTokenClass(this.source_0.charCodeAt(current)) === TC_OTHER) {
      current = current + 1 | 0;
    }
    var $receiver = this.source_0;
    var startIndex = this.currentPosition;
    var endIndex = current;
    var result = $receiver.substring(startIndex, endIndex);
    this.currentPosition = current;
    return result;
  };
  JsonLexer.prototype.appendRange_0 = function (fromIndex, toIndex) {
    this.escapedString_0.append_ezbsdh$(this.source_0, fromIndex, toIndex);
  };
  JsonLexer.prototype.appendEsc_0 = function (startPosition) {
    var tmp$;
    var currentPosition = startPosition;
    var currentChar = this.source_0.charCodeAt((tmp$ = currentPosition, currentPosition = tmp$ + 1 | 0, tmp$));
    if (currentChar === UNICODE_ESC) {
      return this.appendHex_0(this.source_0, currentPosition);
    }var c = escapeToChar(currentChar | 0);
    if (c === INVALID)
      this.fail_bm4lxs$("Invalid escaped char '" + String.fromCharCode(currentChar) + "'");
    this.escapedString_0.append_s8itvh$(c);
    return currentPosition;
  };
  JsonLexer.prototype.appendHex_0 = function (source, startPos) {
    if ((startPos + 4 | 0) >= source.length)
      this.fail_bm4lxs$('Unexpected EOF during unicode escape');
    this.escapedString_0.append_s8itvh$(toChar((this.fromHexChar_0(source, startPos) << 12) + (this.fromHexChar_0(source, startPos + 1 | 0) << 8) + (this.fromHexChar_0(source, startPos + 2 | 0) << 4) + this.fromHexChar_0(source, startPos + 3 | 0) | 0));
    return startPos + 4 | 0;
  };
  JsonLexer.prototype.fromHexChar_0 = function (source, currentPosition) {
    var tmp$;
    var character = source.charCodeAt(currentPosition);
    if ((new CharRange(48, 57)).contains_mef7kx$(character))
      tmp$ = (character | 0) - 48 | 0;
    else if ((new CharRange(97, 102)).contains_mef7kx$(character))
      tmp$ = (character | 0) - 97 + 10 | 0;
    else if ((new CharRange(65, 70)).contains_mef7kx$(character))
      tmp$ = (character | 0) - 65 + 10 | 0;
    else
      tmp$ = this.fail_bm4lxs$("Invalid toHexChar char '" + String.fromCharCode(character) + "' in unicode escape");
    return tmp$;
  };
  JsonLexer.prototype.skipElement_6taknv$ = function (allowLenientStrings) {
    var tokenStack = ArrayList_init();
    var lastToken = this.peekNextToken();
    if (lastToken !== TC_BEGIN_LIST && lastToken !== TC_BEGIN_OBJ) {
      this.consumeStringLenient();
      return;
    }while (true) {
      lastToken = this.peekNextToken();
      if (lastToken === TC_STRING) {
        if (allowLenientStrings)
          this.consumeStringLenient();
        else
          this.consumeKeyString();
        continue;
      }switch (lastToken) {
        case 8:
        case 6:
          tokenStack.add_11rb$(lastToken);
          break;
        case 9:
          if (last(tokenStack) !== TC_BEGIN_LIST)
            throw JsonDecodingException_1(this.currentPosition, 'found ] instead of }', this.source_0);
          removeLast(tokenStack);
          break;
        case 7:
          if (last(tokenStack) !== TC_BEGIN_OBJ)
            throw JsonDecodingException_1(this.currentPosition, 'found } instead of ]', this.source_0);
          removeLast(tokenStack);
          break;
        case 10:
          this.fail_bm4lxs$('Unexpected end of input due to malformed JSON during ignoring unknown keys');
          break;
      }
      this.consumeNextToken();
      if (tokenStack.size === 0)
        return;
    }
  };
  JsonLexer.prototype.toString = function () {
    return "JsonReader(source='" + this.source_0 + "', currentPosition=" + this.currentPosition + ')';
  };
  JsonLexer.prototype.failOnUnknownKey_61zpoe$ = function (key) {
    var $receiver = this.source_0;
    var endIndex = this.currentPosition;
    var processed = $receiver.substring(0, endIndex);
    var lastIndexOf_0 = lastIndexOf(processed, key);
    this.fail_bm4lxs$("Encountered an unknown key '" + key + "'." + '\n' + ignoreUnknownKeysHint, lastIndexOf_0);
  };
  JsonLexer.prototype.fail_bm4lxs$ = function (message, position) {
    if (position === void 0)
      position = this.currentPosition;
    throw JsonDecodingException_1(position, message, this.source_0);
  };
  JsonLexer.prototype.require_wqn2ds$ = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.internal.JsonLexer.require_wqn2ds$', function (condition, position, message) {
    if (position === void 0)
      position = this.currentPosition;
    if (!condition)
      this.fail_bm4lxs$(message(), position);
  });
  JsonLexer.prototype.consumeNumericLiteral = function () {
    var tmp$, tmp$_0;
    var current = this.skipWhitespaces_0();
    if (current === this.source_0.length)
      this.fail_bm4lxs$('EOF');
    if (this.source_0.charCodeAt(current) === STRING) {
      if ((current = current + 1 | 0, current) === this.source_0.length)
        this.fail_bm4lxs$('EOF');
      tmp$ = true;
    } else {
      tmp$ = false;
    }
    var hasQuotation = tmp$;
    var accumulator = L0;
    var isNegative = false;
    var start = current;
    var hasChars = true;
    while (hasChars) {
      var ch = this.source_0.charCodeAt(current);
      if (ch === 45) {
        if (current !== start)
          this.fail_bm4lxs$("Unexpected symbol '-' in numeric literal");
        isNegative = true;
        current = current + 1 | 0;
        continue;
      }var token = charToTokenClass(ch);
      if (token !== TC_OTHER)
        break;
      current = current + 1 | 0;
      hasChars = current !== this.source_0.length;
      var digit = ch - 48;
      if (!(0 <= digit && digit <= 9))
        this.fail_bm4lxs$("Unexpected symbol '" + String.fromCharCode(ch) + "' in numeric literal");
      accumulator = accumulator.multiply(Kotlin.Long.fromInt(10)).subtract(Kotlin.Long.fromInt(digit));
      if (accumulator.toNumber() > 0)
        this.fail_bm4lxs$('Numeric value overflow');
    }
    if (start === current || (isNegative && start === (current - 1 | 0))) {
      this.fail_bm4lxs$('Expected numeric literal');
    }if (hasQuotation) {
      if (!hasChars)
        this.fail_bm4lxs$('EOF');
      if (this.source_0.charCodeAt(current) !== STRING)
        this.fail_bm4lxs$('Expected closing quotation mark');
      current = current + 1 | 0;
    }this.currentPosition = current;
    if (isNegative)
      tmp$_0 = accumulator;
    else if (!equals(accumulator, Long$Companion$MIN_VALUE))
      tmp$_0 = accumulator.unaryMinus();
    else
      tmp$_0 = this.fail_bm4lxs$('Numeric value overflow');
    return tmp$_0;
  };
  JsonLexer.prototype.consumeBoolean = function () {
    return this.consumeBoolean_0(this.skipWhitespaces_0());
  };
  JsonLexer.prototype.consumeBooleanLenient = function () {
    var tmp$;
    var current = this.skipWhitespaces_0();
    if (current === this.source_0.length)
      this.fail_bm4lxs$('EOF');
    if (this.source_0.charCodeAt(current) === STRING) {
      current = current + 1 | 0;
      tmp$ = true;
    } else {
      tmp$ = false;
    }
    var hasQuotation = tmp$;
    var result = this.consumeBoolean_0(current);
    if (hasQuotation) {
      if (this.currentPosition === this.source_0.length)
        this.fail_bm4lxs$('EOF');
      if (this.source_0.charCodeAt(this.currentPosition) !== STRING)
        this.fail_bm4lxs$('Expected closing quotation mark');
      this.currentPosition = this.currentPosition + 1 | 0;
    }return result;
  };
  JsonLexer.prototype.consumeBoolean_0 = function (start) {
    var tmp$, tmp$_0;
    var current = start;
    if (current === this.source_0.length)
      this.fail_bm4lxs$('EOF');
    switch (this.source_0.charCodeAt((tmp$ = current, current = tmp$ + 1 | 0, tmp$)) | 0 | 32) {
      case 116:
        this.consumeBooleanLiteral_0('rue', current);
        tmp$_0 = true;
        break;
      case 102:
        this.consumeBooleanLiteral_0('alse', current);
        tmp$_0 = false;
        break;
      default:tmp$_0 = this.fail_bm4lxs$("Expected valid boolean literal prefix, but had '" + this.consumeStringLenient() + "'");
        break;
    }
    return tmp$_0;
  };
  JsonLexer.prototype.consumeBooleanLiteral_0 = function (literalSuffix, current) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    if ((this.source_0.length - current | 0) < literalSuffix.length) {
      this.fail_bm4lxs$('Unexpected end of boolean literal');
    }tmp$ = get_indices(literalSuffix);
    tmp$_0 = tmp$.first;
    tmp$_1 = tmp$.last;
    tmp$_2 = tmp$.step;
    for (var i = tmp$_0; i <= tmp$_1; i += tmp$_2) {
      var expected = literalSuffix.charCodeAt(i);
      var actual = this.source_0.charCodeAt(current + i | 0);
      if ((expected | 0) !== (actual | 0 | 32)) {
        this.fail_bm4lxs$("Expected valid boolean literal prefix, but had '" + this.consumeStringLenient() + "'");
      }}
    this.currentPosition = current + literalSuffix.length | 0;
  };
  JsonLexer.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonLexer',
    interfaces: []
  };
  var JsonAlternativeNamesKey;
  function buildAlternativeNamesMap$putOrThrow(this$buildAlternativeNamesMap) {
    return function ($receiver, name, index) {
      var tmp$;
      if ((Kotlin.isType(tmp$ = $receiver, Map) ? tmp$ : throwCCE()).containsKey_11rb$(name)) {
        throw new JsonException("The suggested name '" + name + "' for property " + this$buildAlternativeNamesMap.getElementName_za3lpa$(index) + ' is already one of the names for property ' + (this$buildAlternativeNamesMap.getElementName_za3lpa$(getValue($receiver, name)) + ' in ' + this$buildAlternativeNamesMap));
      }$receiver.put_xwzc9p$(name, index);
    };
  }
  function buildAlternativeNamesMap($receiver) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    var putOrThrow = buildAlternativeNamesMap$putOrThrow($receiver);
    var builder = {v: null};
    tmp$ = $receiver.elementsCount;
    for (var i = 0; i < tmp$; i++) {
      var $receiver_0 = $receiver.getElementAnnotations_za3lpa$(i);
      var destination = ArrayList_init();
      var tmp$_3;
      tmp$_3 = $receiver_0.iterator();
      while (tmp$_3.hasNext()) {
        var element = tmp$_3.next();
        if (Kotlin.isType(element, JsonNames))
          destination.add_11rb$(element);
      }
      if ((tmp$_1 = (tmp$_0 = singleOrNull(destination)) != null ? tmp$_0.names : null) != null) {
        var tmp$_4;
        for (tmp$_4 = 0; tmp$_4 !== tmp$_1.length; ++tmp$_4) {
          var element_0 = tmp$_1[tmp$_4];
          if (builder.v == null)
            builder.v = createMapForCache($receiver.elementsCount);
          putOrThrow(ensureNotNull(builder.v), element_0, i);
        }
      }}
    return (tmp$_2 = builder.v) != null ? tmp$_2 : emptyMap();
  }
  function getJsonNameIndex($receiver, json, name) {
    var tmp$;
    var index = $receiver.getElementIndex_61zpoe$(name);
    if (index !== -3)
      return index;
    if (!json.configuration.useAlternativeNames)
      return index;
    var alternativeNamesMap = json.schemaCache_8be2vx$.getOrPut_a5unbe$($receiver, JsonAlternativeNamesKey, getCallableRef('buildAlternativeNamesMap', function ($receiver) {
      return buildAlternativeNamesMap($receiver);
    }.bind(null, $receiver)));
    return (tmp$ = alternativeNamesMap.get_11rb$(name)) != null ? tmp$ : -3;
  }
  function getJsonNameIndexOrThrow($receiver, json, name) {
    var index = getJsonNameIndex($receiver, json, name);
    if (index === -3)
      throw SerializationException_init($receiver.serialName + " does not contain element with name '" + name + "'");
    return index;
  }
  function JsonTreeReader(configuration, lexer) {
    this.lexer_0 = lexer;
    this.isLenient_0 = configuration.isLenient;
  }
  JsonTreeReader.prototype.readObject_0 = function () {
    var lastToken = this.lexer_0.consumeNextToken_s8j3t7$(TC_BEGIN_OBJ);
    if (this.lexer_0.peekNextToken() === TC_COMMA)
      this.lexer_0.fail_bm4lxs$('Unexpected leading comma');
    var result = LinkedHashMap_init();
    while (this.lexer_0.canConsumeValue()) {
      var key = this.isLenient_0 ? this.lexer_0.consumeStringLenient() : this.lexer_0.consumeString();
      this.lexer_0.consumeNextToken_s8j3t7$(TC_COLON);
      var element = this.read();
      result.put_xwzc9p$(key, element);
      lastToken = this.lexer_0.consumeNextToken();
      if (lastToken !== TC_COMMA && lastToken !== TC_END_OBJ) {
        this.lexer_0.fail_bm4lxs$('Expected end of the object or comma');
      }}
    if (lastToken === TC_BEGIN_OBJ) {
      this.lexer_0.consumeNextToken_s8j3t7$(TC_END_OBJ);
    } else if (lastToken === TC_COMMA) {
      this.lexer_0.fail_bm4lxs$('Unexpected trailing comma');
    }return new JsonObject(result);
  };
  JsonTreeReader.prototype.readArray_0 = function () {
    var lastToken = this.lexer_0.consumeNextToken();
    if (this.lexer_0.peekNextToken() === TC_COMMA)
      this.lexer_0.fail_bm4lxs$('Unexpected leading comma');
    var result = ArrayList_init();
    while (this.lexer_0.canConsumeValue()) {
      var element = this.read();
      result.add_11rb$(element);
      lastToken = this.lexer_0.consumeNextToken();
      if (lastToken !== TC_COMMA) {
        var $this = this.lexer_0;
        var condition = lastToken === TC_END_LIST;
        var position;
        position = $this.currentPosition;
        if (!condition) {
          $this.fail_bm4lxs$('Expected end of the array or comma', position);
        }}}
    if (lastToken === TC_BEGIN_LIST) {
      this.lexer_0.consumeNextToken_s8j3t7$(TC_END_LIST);
    } else if (lastToken === TC_COMMA) {
      this.lexer_0.fail_bm4lxs$('Unexpected trailing comma');
    }return new JsonArray(result);
  };
  JsonTreeReader.prototype.readValue_0 = function (isString) {
    var tmp$;
    if (this.isLenient_0 || !isString) {
      tmp$ = this.lexer_0.consumeStringLenient();
    } else {
      tmp$ = this.lexer_0.consumeString();
    }
    var string = tmp$;
    if (!isString && equals(string, NULL))
      return JsonNull_getInstance();
    return new JsonLiteral(string, isString);
  };
  JsonTreeReader.prototype.read = function () {
    var tmp$;
    switch (this.lexer_0.peekNextToken()) {
      case 1:
        tmp$ = this.readValue_0(true);
        break;
      case 0:
        tmp$ = this.readValue_0(false);
        break;
      case 6:
        tmp$ = this.readObject_0();
        break;
      case 8:
        tmp$ = this.readArray_0();
        break;
      default:tmp$ = this.lexer_0.fail_bm4lxs$("Can't begin reading element, unexpected token");
        break;
    }
    return tmp$;
  };
  JsonTreeReader.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeReader',
    interfaces: []
  };
  var encodePolymorphically = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.internal.encodePolymorphically_51w9c5$', wrapFunction(function () {
    var AbstractPolymorphicSerializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.internal.AbstractPolymorphicSerializer;
    var SerializationStrategy = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.SerializationStrategy;
    var throwCCE = Kotlin.throwCCE;
    var Any = Object;
    return function ($receiver, serializer, value, ifPolymorphic) {
      var tmp$, tmp$_0;
      if (!Kotlin.isType(serializer, AbstractPolymorphicSerializer) || $receiver.json.configuration.useArrayPolymorphism) {
        serializer.serialize_55azsf$($receiver, value);
        return;
      }var actualSerializer = findActualSerializer($receiver, Kotlin.isType(tmp$ = serializer, SerializationStrategy) ? tmp$ : throwCCE(), Kotlin.isType(tmp$_0 = value, Any) ? tmp$_0 : throwCCE());
      ifPolymorphic();
      actualSerializer.serialize_55azsf$($receiver, value);
    };
  }));
  function findActualSerializer($receiver, serializer, value) {
    var tmp$, tmp$_0;
    var casted = Kotlin.isType(tmp$ = serializer, AbstractPolymorphicSerializer) ? tmp$ : throwCCE();
    var actualSerializer = findPolymorphicSerializer(casted, $receiver, Kotlin.isType(tmp$_0 = value, Any) ? tmp$_0 : throwCCE());
    validateIfSealed(casted, actualSerializer, $receiver.json.configuration.classDiscriminator);
    var kind = actualSerializer.descriptor.kind;
    checkKind(kind);
    return actualSerializer;
  }
  function validateIfSealed(serializer, actualSerializer, classDiscriminator) {
    if (!Kotlin.isType(serializer, SealedClassSerializer))
      return;
    if (jsonCachedSerialNames(actualSerializer.descriptor).contains_11rb$(classDiscriminator)) {
      var baseName = serializer.descriptor.serialName;
      var actualName = actualSerializer.descriptor.serialName;
      throw IllegalStateException_init(("Sealed class '" + actualName + "' cannot be serialized as base class '" + baseName + "' because" + (" it has property name that conflicts with JSON class discriminator '" + classDiscriminator + "'. ") + 'You can either change class discriminator in JsonConfiguration, ' + 'rename property with @SerialName annotation or fall back to array polymorphism').toString());
    }}
  function checkKind(kind) {
    if (Kotlin.isType(kind, Object.getPrototypeOf(SerialKind.ENUM).constructor)) {
      throw IllegalStateException_init("Enums cannot be serialized polymorphically with 'type' parameter. You can use 'JsonBuilder.useArrayPolymorphism' instead".toString());
    }if (Kotlin.isType(kind, PrimitiveKind)) {
      throw IllegalStateException_init("Primitives cannot be serialized polymorphically with 'type' parameter. You can use 'JsonBuilder.useArrayPolymorphism' instead".toString());
    }if (Kotlin.isType(kind, PolymorphicKind)) {
      throw IllegalStateException_init('Actual serializer for polymorphic cannot be polymorphic itself'.toString());
    }}
  function decodeSerializableValuePolymorphic($receiver, deserializer) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    if (!Kotlin.isType(deserializer, AbstractPolymorphicSerializer) || $receiver.json.configuration.useArrayPolymorphism) {
      return deserializer.deserialize_bq71mq$($receiver);
    }var value = $receiver.decodeJsonElement();
    var descriptor = deserializer.descriptor;
    if (!Kotlin.isType(value, JsonObject)) {
      throw JsonDecodingException_0(-1, 'Expected ' + getKClass(JsonObject) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(value));
    }var jsonTree = value;
    var discriminator = $receiver.json.configuration.classDiscriminator;
    var type = (tmp$_0 = (tmp$ = jsonTree.get_11rb$(discriminator)) != null ? get_jsonPrimitive(tmp$) : null) != null ? tmp$_0.content : null;
    var actualSerializer = (tmp$_1 = deserializer.findPolymorphicSerializerOrNull_ca6uye$($receiver, type)) != null ? tmp$_1 : throwSerializerNotFound(type, jsonTree);
    return readPolymorphicJson($receiver.json, discriminator, jsonTree, Kotlin.isType(tmp$_2 = actualSerializer, DeserializationStrategy) ? tmp$_2 : throwCCE());
  }
  function throwSerializerNotFound(type, jsonTree) {
    var suffix = type == null ? "missing class discriminator ('null')" : "class discriminator '" + toString(type) + "'";
    throw JsonDecodingException_1(-1, 'Polymorphic serializer was not found for ' + suffix, jsonTree.toString());
  }
  function PolymorphismValidator(useArrayPolymorphism, discriminator) {
    this.useArrayPolymorphism_0 = useArrayPolymorphism;
    this.discriminator_0 = discriminator;
  }
  PolymorphismValidator.prototype.contextual_vhoqnv$ = function (kClass, provider) {
  };
  PolymorphismValidator.prototype.polymorphic_kfyidi$ = function (baseClass, actualClass, actualSerializer) {
    var descriptor = actualSerializer.descriptor;
    this.checkKind_0(descriptor, actualClass);
    if (!this.useArrayPolymorphism_0) {
      this.checkDiscriminatorCollisions_0(descriptor, actualClass);
    }};
  PolymorphismValidator.prototype.checkKind_0 = function (descriptor, actualClass) {
    var kind = descriptor.kind;
    if (Kotlin.isType(kind, PolymorphicKind) || equals(kind, SerialKind.CONTEXTUAL)) {
      throw IllegalArgumentException_init('Serializer for ' + toString(actualClass.simpleName) + " can't be registered as a subclass for polymorphic serialization " + ('because its kind ' + kind + ' is not concrete. To work with multiple hierarchies, register it as a base class.'));
    }if (this.useArrayPolymorphism_0)
      return;
    if (equals(kind, StructureKind.LIST) || equals(kind, StructureKind.MAP) || Kotlin.isType(kind, PrimitiveKind) || Kotlin.isType(kind, Object.getPrototypeOf(SerialKind.ENUM).constructor)) {
      throw IllegalArgumentException_init('Serializer for ' + toString(actualClass.simpleName) + ' of kind ' + kind + ' cannot be serialized polymorphically with class discriminator.');
    }};
  PolymorphismValidator.prototype.checkDiscriminatorCollisions_0 = function (descriptor, actualClass) {
    var tmp$;
    tmp$ = descriptor.elementsCount;
    for (var i = 0; i < tmp$; i++) {
      var name = descriptor.getElementName_za3lpa$(i);
      if (equals(name, this.discriminator_0)) {
        throw IllegalArgumentException_init('Polymorphic serializer for ' + actualClass + " has property '" + name + "' that conflicts " + 'with JSON class discriminator. You can either change class discriminator in JsonConfiguration, ' + 'rename property with @SerialName annotation ' + 'or fall back to array polymorphism');
      }}
  };
  PolymorphismValidator.prototype.polymorphicDefault_yd5wsm$ = function (baseClass, defaultSerializerProvider) {
  };
  PolymorphismValidator.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'PolymorphismValidator',
    interfaces: [SerializersModuleCollector]
  };
  function DescriptorSchemaCache() {
    this.map_0 = createMapForCache(1);
  }
  DescriptorSchemaCache.prototype.set_cldnac$ = function (descriptor, key, value) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    var $receiver = this.map_0;
    var tmp$_3;
    var value_0 = $receiver.get_11rb$(descriptor);
    if (value_0 == null) {
      var answer = createMapForCache(1);
      $receiver.put_xwzc9p$(descriptor, answer);
      tmp$_3 = answer;
    } else {
      tmp$_3 = value_0;
    }
    tmp$_2 = tmp$_3;
    tmp$_1 = Kotlin.isType(tmp$_0 = key, DescriptorSchemaCache$Key) ? tmp$_0 : throwCCE();
    var value_1 = Kotlin.isType(tmp$ = value, Any) ? tmp$ : throwCCE();
    tmp$_2.put_xwzc9p$(tmp$_1, value_1);
  };
  DescriptorSchemaCache.prototype.getOrPut_a5unbe$ = function (descriptor, key, defaultValue) {
    var tmp$;
    if ((tmp$ = this.get_khmtuq$(descriptor, key)) != null) {
      return tmp$;
    }var value = defaultValue();
    this.set_cldnac$(descriptor, key, value);
    return value;
  };
  DescriptorSchemaCache.prototype.get_khmtuq$ = function (descriptor, key) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    tmp$_1 = this.map_0.get_11rb$(descriptor);
    tmp$_0 = Kotlin.isType(tmp$ = key, DescriptorSchemaCache$Key) ? tmp$ : throwCCE();
    return Kotlin.isType(tmp$_2 = tmp$_1 != null ? tmp$_1.get_11rb$(tmp$_0) : null, Any) ? tmp$_2 : null;
  };
  function DescriptorSchemaCache$Key() {
  }
  DescriptorSchemaCache$Key.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Key',
    interfaces: []
  };
  DescriptorSchemaCache.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DescriptorSchemaCache',
    interfaces: []
  };
  function StreamingJsonDecoder(json, mode, lexer) {
    AbstractDecoder.call(this);
    this.json_n7tmf6$_0 = json;
    this.mode_0 = mode;
    this.lexer_8be2vx$ = lexer;
    this.serializersModule_5carun$_0 = this.json.serializersModule;
    this.currentIndex_0 = -1;
    this.configuration_0 = this.json.configuration;
  }
  Object.defineProperty(StreamingJsonDecoder.prototype, 'json', {
    get: function () {
      return this.json_n7tmf6$_0;
    }
  });
  Object.defineProperty(StreamingJsonDecoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.serializersModule_5carun$_0;
    }
  });
  StreamingJsonDecoder.prototype.decodeJsonElement = function () {
    return (new JsonTreeReader(this.json.configuration, this.lexer_8be2vx$)).read();
  };
  StreamingJsonDecoder.prototype.decodeSerializableValue_w63s0f$ = function (deserializer) {
    return decodeSerializableValuePolymorphic(this, deserializer);
  };
  StreamingJsonDecoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    var tmp$;
    var newMode = switchMode(this.json, descriptor);
    this.lexer_8be2vx$.consumeNextToken_s8itvh$(unboxChar(newMode.begin));
    this.checkLeadingComma_0();
    switch (newMode.name) {
      case 'LIST':
      case 'MAP':
      case 'POLY_OBJ':
        tmp$ = new StreamingJsonDecoder(this.json, newMode, this.lexer_8be2vx$);
        break;
      default:if (this.mode_0 === newMode) {
          tmp$ = this;
        } else {
          tmp$ = new StreamingJsonDecoder(this.json, newMode, this.lexer_8be2vx$);
        }

        break;
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.endStructure_24f42q$ = function (descriptor) {
    this.lexer_8be2vx$.consumeNextToken_s8itvh$(unboxChar(this.mode_0.end));
  };
  StreamingJsonDecoder.prototype.decodeNotNullMark = function () {
    return this.lexer_8be2vx$.tryConsumeNotNull();
  };
  StreamingJsonDecoder.prototype.decodeNull = function () {
    return null;
  };
  StreamingJsonDecoder.prototype.checkLeadingComma_0 = function () {
    if (this.lexer_8be2vx$.peekNextToken() === TC_COMMA) {
      this.lexer_8be2vx$.fail_bm4lxs$('Unexpected leading comma');
    }};
  StreamingJsonDecoder.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    var tmp$;
    switch (this.mode_0.name) {
      case 'OBJ':
        tmp$ = this.decodeObjectIndex_0(descriptor);
        break;
      case 'MAP':
        tmp$ = this.decodeMapIndex_0();
        break;
      default:tmp$ = this.decodeListIndex_0();
        break;
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.decodeMapIndex_0 = function () {
    var tmp$;
    var hasComma = false;
    var decodingKey = this.currentIndex_0 % 2 !== 0;
    if (decodingKey) {
      if (this.currentIndex_0 !== -1) {
        hasComma = this.lexer_8be2vx$.tryConsumeComma();
      }} else {
      this.lexer_8be2vx$.consumeNextToken_s8itvh$(COLON);
    }
    if (this.lexer_8be2vx$.canConsumeValue()) {
      if (decodingKey) {
        if (this.currentIndex_0 === -1) {
          var $this = this.lexer_8be2vx$;
          var condition = !hasComma;
          var position;
          position = $this.currentPosition;
          if (!condition) {
            $this.fail_bm4lxs$('Unexpected trailing comma', position);
          }} else {
          var $this_0 = this.lexer_8be2vx$;
          var condition_0 = hasComma;
          var position_0;
          position_0 = $this_0.currentPosition;
          if (!condition_0) {
            $this_0.fail_bm4lxs$('Expected comma after the key-value pair', position_0);
          }}
      }tmp$ = (this.currentIndex_0 = this.currentIndex_0 + 1 | 0, this.currentIndex_0);
    } else {
      if (hasComma)
        this.lexer_8be2vx$.fail_bm4lxs$("Expected '}', but had ',' instead");
      tmp$ = -1;
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.coerceInputValue_0 = function (descriptor, index) {
    var tmp$;
    var elementDescriptor = descriptor.getElementDescriptor_za3lpa$(index);
    if (!elementDescriptor.isNullable && !this.lexer_8be2vx$.tryConsumeNotNull())
      return true;
    if (equals(elementDescriptor.kind, SerialKind.ENUM)) {
      tmp$ = this.lexer_8be2vx$.peekString_6taknv$(this.configuration_0.isLenient);
      if (tmp$ == null) {
        return false;
      }var enumValue = tmp$;
      var enumIndex = getJsonNameIndex(elementDescriptor, this.json, enumValue);
      if (enumIndex === -3) {
        this.lexer_8be2vx$.consumeString();
        return true;
      }}return false;
  };
  StreamingJsonDecoder.prototype.decodeObjectIndex_0 = function (descriptor) {
    var tmp$;
    var hasComma = this.lexer_8be2vx$.tryConsumeComma();
    while (this.lexer_8be2vx$.canConsumeValue()) {
      hasComma = false;
      var key = this.decodeStringKey_0();
      this.lexer_8be2vx$.consumeNextToken_s8itvh$(COLON);
      var index = getJsonNameIndex(descriptor, this.json, key);
      if (index !== -3) {
        if (this.configuration_0.coerceInputValues && this.coerceInputValue_0(descriptor, index)) {
          hasComma = this.lexer_8be2vx$.tryConsumeComma();
          tmp$ = false;
        } else {
          return index;
        }
      } else {
        tmp$ = true;
      }
      var isUnknown = tmp$;
      if (isUnknown) {
        hasComma = this.handleUnknown_0(key);
      }}
    if (hasComma)
      this.lexer_8be2vx$.fail_bm4lxs$('Unexpected trailing comma');
    return -1;
  };
  StreamingJsonDecoder.prototype.handleUnknown_0 = function (key) {
    if (this.configuration_0.ignoreUnknownKeys) {
      this.lexer_8be2vx$.skipElement_6taknv$(this.configuration_0.isLenient);
    } else {
      this.lexer_8be2vx$.failOnUnknownKey_61zpoe$(key);
    }
    return this.lexer_8be2vx$.tryConsumeComma();
  };
  StreamingJsonDecoder.prototype.decodeListIndex_0 = function () {
    var tmp$;
    var hasComma = this.lexer_8be2vx$.tryConsumeComma();
    if (this.lexer_8be2vx$.canConsumeValue()) {
      if (this.currentIndex_0 !== -1 && !hasComma)
        this.lexer_8be2vx$.fail_bm4lxs$('Expected end of the array or comma');
      tmp$ = (this.currentIndex_0 = this.currentIndex_0 + 1 | 0, this.currentIndex_0);
    } else {
      if (hasComma)
        this.lexer_8be2vx$.fail_bm4lxs$('Unexpected trailing comma');
      tmp$ = -1;
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.decodeBoolean = function () {
    var tmp$;
    if (this.configuration_0.isLenient) {
      tmp$ = this.lexer_8be2vx$.consumeBooleanLenient();
    } else {
      tmp$ = this.lexer_8be2vx$.consumeBoolean();
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.decodeByte = function () {
    var value = this.lexer_8be2vx$.consumeNumericLiteral();
    if (!equals(value, Kotlin.Long.fromInt(toByte(value.toInt()))))
      this.lexer_8be2vx$.fail_bm4lxs$("Failed to parse byte for input '" + value.toString() + "'");
    return toByte(value.toInt());
  };
  StreamingJsonDecoder.prototype.decodeShort = function () {
    var value = this.lexer_8be2vx$.consumeNumericLiteral();
    if (!equals(value, Kotlin.Long.fromInt(toShort(value.toInt()))))
      this.lexer_8be2vx$.fail_bm4lxs$("Failed to parse short for input '" + value.toString() + "'");
    return toShort(value.toInt());
  };
  StreamingJsonDecoder.prototype.decodeInt = function () {
    var value = this.lexer_8be2vx$.consumeNumericLiteral();
    if (!equals(value, Kotlin.Long.fromInt(value.toInt())))
      this.lexer_8be2vx$.fail_bm4lxs$("Failed to parse int for input '" + value.toString() + "'");
    return value.toInt();
  };
  StreamingJsonDecoder.prototype.decodeLong = function () {
    return this.lexer_8be2vx$.consumeNumericLiteral();
  };
  StreamingJsonDecoder.prototype.decodeFloat = function () {
    var $receiver = this.lexer_8be2vx$;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toDouble(input);
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'float' + "' for input '" + input + "'");
      } else
        throw e;
    }
    var result = parseString$result;
    var specialFp = this.json.configuration.allowSpecialFloatingPointValues;
    if (specialFp || isFinite(result))
      return result;
    throwInvalidFloatingPointDecoded(this.lexer_8be2vx$, result);
  };
  StreamingJsonDecoder.prototype.decodeDouble = function () {
    var $receiver = this.lexer_8be2vx$;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toDouble(input);
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'double' + "' for input '" + input + "'");
      } else
        throw e;
    }
    var result = parseString$result;
    var specialFp = this.json.configuration.allowSpecialFloatingPointValues;
    if (specialFp || isFinite_0(result))
      return result;
    throwInvalidFloatingPointDecoded(this.lexer_8be2vx$, result);
  };
  StreamingJsonDecoder.prototype.decodeChar = function () {
    var string = this.lexer_8be2vx$.consumeStringLenient();
    if (string.length !== 1)
      this.lexer_8be2vx$.fail_bm4lxs$("Expected single char, but got '" + string + "'");
    return toBoxedChar(string.charCodeAt(0));
  };
  StreamingJsonDecoder.prototype.decodeStringKey_0 = function () {
    var tmp$;
    if (this.configuration_0.isLenient) {
      tmp$ = this.lexer_8be2vx$.consumeStringLenient();
    } else {
      tmp$ = this.lexer_8be2vx$.consumeKeyString();
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.decodeString = function () {
    var tmp$;
    if (this.configuration_0.isLenient) {
      tmp$ = this.lexer_8be2vx$.consumeStringLenient();
    } else {
      tmp$ = this.lexer_8be2vx$.consumeString();
    }
    return tmp$;
  };
  StreamingJsonDecoder.prototype.decodeInline_24f42q$ = function (inlineDescriptor) {
    return get_isUnsignedNumber(inlineDescriptor) ? new JsonDecoderForUnsignedTypes(this.lexer_8be2vx$, this.json) : AbstractDecoder.prototype.decodeInline_24f42q$.call(this, inlineDescriptor);
  };
  StreamingJsonDecoder.prototype.decodeEnum_24f42q$ = function (enumDescriptor) {
    return getJsonNameIndexOrThrow(enumDescriptor, this.json, this.decodeString());
  };
  StreamingJsonDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'StreamingJsonDecoder',
    interfaces: [AbstractDecoder, JsonDecoder]
  };
  function JsonDecoderForUnsignedTypes(lexer, json) {
    AbstractDecoder.call(this);
    this.lexer_0 = lexer;
    this.serializersModule_ftyxok$_0 = json.serializersModule;
  }
  Object.defineProperty(JsonDecoderForUnsignedTypes.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.serializersModule_ftyxok$_0;
    }
  });
  JsonDecoderForUnsignedTypes.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    throw IllegalStateException_init('unsupported'.toString());
  };
  JsonDecoderForUnsignedTypes.prototype.decodeInt = function () {
    var $receiver = this.lexer_0;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toUInt(input).data;
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'UInt' + "' for input '" + input + "'");
      } else
        throw e;
    }
    return parseString$result;
  };
  JsonDecoderForUnsignedTypes.prototype.decodeLong = function () {
    var $receiver = this.lexer_0;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toULong(input).data;
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'ULong' + "' for input '" + input + "'");
      } else
        throw e;
    }
    return parseString$result;
  };
  JsonDecoderForUnsignedTypes.prototype.decodeByte = function () {
    var $receiver = this.lexer_0;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toUByte(input).data;
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'UByte' + "' for input '" + input + "'");
      } else
        throw e;
    }
    return parseString$result;
  };
  JsonDecoderForUnsignedTypes.prototype.decodeShort = function () {
    var $receiver = this.lexer_0;
    var parseString$result;
    var input = $receiver.consumeStringLenient();
    try {
      parseString$result = toUShort(input).data;
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + 'UShort' + "' for input '" + input + "'");
      } else
        throw e;
    }
    return parseString$result;
  };
  JsonDecoderForUnsignedTypes.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonDecoderForUnsignedTypes',
    interfaces: [AbstractDecoder]
  };
  function parseString($receiver, expectedType, block) {
    var input = $receiver.consumeStringLenient();
    try {
      return block(input);
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        $receiver.fail_bm4lxs$("Failed to parse type '" + expectedType + "' for input '" + input + "'");
      } else
        throw e;
    }
  }
  var unsignedNumberDescriptors;
  function get_isUnsignedNumber($receiver) {
    return $receiver.isInline && unsignedNumberDescriptors.contains_11rb$($receiver);
  }
  function StreamingJsonEncoder(composer, json, mode, modeReuseCache) {
    AbstractEncoder.call(this);
    this.composer_0 = composer;
    this.json_8uu1uy$_0 = json;
    this.mode_0 = mode;
    this.modeReuseCache_0 = modeReuseCache;
    this.serializersModule_tvtxsn$_0 = this.json.serializersModule;
    this.configuration_0 = this.json.configuration;
    this.forceQuoting_0 = false;
    this.writePolymorphic_0 = false;
    var i = this.mode_0.ordinal;
    if (this.modeReuseCache_0 != null) {
      if (this.modeReuseCache_0[i] !== null || this.modeReuseCache_0[i] !== this)
        this.modeReuseCache_0[i] = this;
    }}
  Object.defineProperty(StreamingJsonEncoder.prototype, 'json', {
    get: function () {
      return this.json_8uu1uy$_0;
    }
  });
  Object.defineProperty(StreamingJsonEncoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.serializersModule_tvtxsn$_0;
    }
  });
  StreamingJsonEncoder.prototype.encodeJsonElement_qiw0cd$ = function (element) {
    this.encodeSerializableValue_tf03ej$(JsonElementSerializer_getInstance(), element);
  };
  StreamingJsonEncoder.prototype.shouldEncodeElementDefault_szpzho$ = function (descriptor, index) {
    return this.configuration_0.encodeDefaults;
  };
  StreamingJsonEncoder.prototype.encodeSerializableValue_tf03ej$ = function (serializer, value) {
    encodePolymorphically$break: do {
      var tmp$, tmp$_0;
      if (!Kotlin.isType(serializer, AbstractPolymorphicSerializer) || this.json.configuration.useArrayPolymorphism) {
        serializer.serialize_55azsf$(this, value);
        break encodePolymorphically$break;
      }var actualSerializer = findActualSerializer(this, Kotlin.isType(tmp$ = serializer, SerializationStrategy) ? tmp$ : throwCCE(), Kotlin.isType(tmp$_0 = value, Any) ? tmp$_0 : throwCCE());
      this.writePolymorphic_0 = true;
      actualSerializer.serialize_55azsf$(this, value);
    }
     while (false);
  };
  StreamingJsonEncoder.prototype.encodeTypeInfo_0 = function (descriptor) {
    this.composer_0.nextItem();
    this.encodeString_61zpoe$(this.configuration_0.classDiscriminator);
    this.composer_0.print_s8itvh$(COLON);
    this.composer_0.space();
    this.encodeString_61zpoe$(descriptor.serialName);
  };
  StreamingJsonEncoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0;
    var newMode = switchMode(this.json, descriptor);
    if (unboxChar(newMode.begin) !== INVALID) {
      this.composer_0.print_s8itvh$(unboxChar(newMode.begin));
      this.composer_0.indent();
    }if (this.writePolymorphic_0) {
      this.writePolymorphic_0 = false;
      this.encodeTypeInfo_0(descriptor);
    }if (this.mode_0 === newMode) {
      return this;
    }return (tmp$_0 = (tmp$ = this.modeReuseCache_0) != null ? tmp$[newMode.ordinal] : null) != null ? tmp$_0 : new StreamingJsonEncoder(this.composer_0, this.json, newMode, this.modeReuseCache_0);
  };
  StreamingJsonEncoder.prototype.endStructure_24f42q$ = function (descriptor) {
    if (unboxChar(this.mode_0.end) !== INVALID) {
      this.composer_0.unIndent();
      this.composer_0.nextItem();
      this.composer_0.print_s8itvh$(unboxChar(this.mode_0.end));
    }};
  StreamingJsonEncoder.prototype.encodeElement_szpzho$ = function (descriptor, index) {
    var tmp$;
    switch (this.mode_0.name) {
      case 'LIST':
        if (!this.composer_0.writingFirst)
          this.composer_0.print_s8itvh$(COMMA);
        this.composer_0.nextItem();
        break;
      case 'MAP':
        if (!this.composer_0.writingFirst) {
          if (index % 2 === 0) {
            this.composer_0.print_s8itvh$(COMMA);
            this.composer_0.nextItem();
            tmp$ = true;
          } else {
            this.composer_0.print_s8itvh$(COLON);
            this.composer_0.space();
            tmp$ = false;
          }
          this.forceQuoting_0 = tmp$;
        } else {
          this.forceQuoting_0 = true;
          this.composer_0.nextItem();
        }

        break;
      case 'POLY_OBJ':
        if (index === 0)
          this.forceQuoting_0 = true;
        if (index === 1) {
          this.composer_0.print_s8itvh$(COMMA);
          this.composer_0.space();
          this.forceQuoting_0 = false;
        }
        break;
      default:if (!this.composer_0.writingFirst)
          this.composer_0.print_s8itvh$(COMMA);
        this.composer_0.nextItem();
        this.encodeString_61zpoe$(descriptor.getElementName_za3lpa$(index));
        this.composer_0.print_s8itvh$(COLON);
        this.composer_0.space();
        break;
    }
    return true;
  };
  StreamingJsonEncoder.prototype.encodeInline_24f42q$ = function (inlineDescriptor) {
    return get_isUnsignedNumber(inlineDescriptor) ? new StreamingJsonEncoder(new ComposerForUnsignedNumbers(this.composer_0.sb_8be2vx$, this.json), this.json, this.mode_0, null) : AbstractEncoder.prototype.encodeInline_24f42q$.call(this, inlineDescriptor);
  };
  StreamingJsonEncoder.prototype.encodeNull = function () {
    this.composer_0.print_61zpoe$(NULL);
  };
  StreamingJsonEncoder.prototype.encodeBoolean_6taknv$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_6taknv$(value);
  };
  StreamingJsonEncoder.prototype.encodeByte_s8j3t7$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_s8j3t7$(value);
  };
  StreamingJsonEncoder.prototype.encodeShort_mq22fl$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_mq22fl$(value);
  };
  StreamingJsonEncoder.prototype.encodeInt_za3lpa$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_za3lpa$(value);
  };
  StreamingJsonEncoder.prototype.encodeLong_s8cxhz$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_s8cxhz$(value);
  };
  StreamingJsonEncoder.prototype.encodeFloat_mx4ult$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_mx4ult$(value);
    if (!this.configuration_0.allowSpecialFloatingPointValues && !isFinite(value)) {
      throw InvalidFloatingPointEncoded(value, this.composer_0.sb_8be2vx$.toString());
    }};
  StreamingJsonEncoder.prototype.encodeDouble_14dthe$ = function (value) {
    if (this.forceQuoting_0)
      this.encodeString_61zpoe$(value.toString());
    else
      this.composer_0.print_14dthe$(value);
    if (!this.configuration_0.allowSpecialFloatingPointValues && !isFinite_0(value)) {
      throw InvalidFloatingPointEncoded(value, this.composer_0.sb_8be2vx$.toString());
    }};
  StreamingJsonEncoder.prototype.encodeChar_s8itvh$ = function (value) {
    this.encodeString_61zpoe$(String.fromCharCode(value));
  };
  StreamingJsonEncoder.prototype.encodeString_61zpoe$ = function (value) {
    this.composer_0.printQuoted_61zpoe$(value);
  };
  StreamingJsonEncoder.prototype.encodeEnum_szpzho$ = function (enumDescriptor, index) {
    this.encodeString_61zpoe$(enumDescriptor.getElementName_za3lpa$(index));
  };
  StreamingJsonEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'StreamingJsonEncoder',
    interfaces: [AbstractEncoder, JsonEncoder]
  };
  function StreamingJsonEncoder_init(output, json, mode, modeReuseCache, $this) {
    $this = $this || Object.create(StreamingJsonEncoder.prototype);
    StreamingJsonEncoder.call($this, new Composer(output, json), json, mode, modeReuseCache);
    return $this;
  }
  function toHexChar(i) {
    var d = i & 15;
    return d < 10 ? toChar(d + 48 | 0) : toChar(d - 10 + 97 | 0);
  }
  var ESCAPE_STRINGS;
  var ESCAPE_MARKERS;
  function printQuoted($receiver, value) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    $receiver.append_s8itvh$(STRING);
    var lastPos = 0;
    tmp$ = get_indices(value);
    tmp$_0 = tmp$.first;
    tmp$_1 = tmp$.last;
    tmp$_2 = tmp$.step;
    for (var i = tmp$_0; i <= tmp$_1; i += tmp$_2) {
      var c = value.charCodeAt(i) | 0;
      if (c < ESCAPE_STRINGS.length && ESCAPE_STRINGS[c] != null) {
        $receiver.append_ezbsdh$(value, lastPos, i);
        $receiver.append_pdl1vj$(ESCAPE_STRINGS[c]);
        lastPos = i + 1 | 0;
      }}
    if (lastPos !== 0)
      $receiver.append_ezbsdh$(value, lastPos, value.length);
    else
      $receiver.append_pdl1vj$(value);
    $receiver.append_s8itvh$(STRING);
  }
  function toBooleanStrictOrNull($receiver) {
    if (equals_0($receiver, 'true', true))
      return true;
    else if (equals_0($receiver, 'false', true))
      return false;
    else
      return null;
  }
  function readJson($receiver, element, deserializer) {
    var tmp$, tmp$_0;
    if (Kotlin.isType(element, JsonObject))
      tmp$_0 = new JsonTreeDecoder($receiver, element);
    else if (Kotlin.isType(element, JsonArray))
      tmp$_0 = new JsonTreeListDecoder($receiver, element);
    else if (Kotlin.isType(element, JsonLiteral) || equals(element, JsonNull_getInstance())) {
      tmp$_0 = new JsonPrimitiveDecoder($receiver, Kotlin.isType(tmp$ = element, JsonPrimitive) ? tmp$ : throwCCE());
    } else
      tmp$_0 = Kotlin.noWhenBranchMatched();
    var input = tmp$_0;
    return input.decodeSerializableValue_w63s0f$(deserializer);
  }
  function readPolymorphicJson($receiver, discriminator, element, deserializer) {
    return (new JsonTreeDecoder($receiver, element, discriminator, deserializer.descriptor)).decodeSerializableValue_w63s0f$(deserializer);
  }
  function AbstractJsonTreeDecoder(json, value) {
    NamedValueDecoder.call(this);
    this.json_nf7dq8$_0 = json;
    this.value_admuh1$_0 = value;
    this.configuration_0 = this.json.configuration;
  }
  Object.defineProperty(AbstractJsonTreeDecoder.prototype, 'json', {
    get: function () {
      return this.json_nf7dq8$_0;
    }
  });
  Object.defineProperty(AbstractJsonTreeDecoder.prototype, 'value', {
    get: function () {
      return this.value_admuh1$_0;
    }
  });
  Object.defineProperty(AbstractJsonTreeDecoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.json.serializersModule;
    }
  });
  AbstractJsonTreeDecoder.prototype.currentObject_0 = function () {
    var tmp$, tmp$_0;
    return (tmp$_0 = (tmp$ = this.currentTagOrNull) != null ? this.currentElement_61zpoe$(tmp$) : null) != null ? tmp$_0 : this.value;
  };
  AbstractJsonTreeDecoder.prototype.decodeJsonElement = function () {
    return this.currentObject_0();
  };
  AbstractJsonTreeDecoder.prototype.decodeSerializableValue_w63s0f$ = function (deserializer) {
    return decodeSerializableValuePolymorphic(this, deserializer);
  };
  AbstractJsonTreeDecoder.prototype.composeName_puj7f4$ = function (parentName, childName) {
    return childName;
  };
  AbstractJsonTreeDecoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0;
    var currentObject = this.currentObject_0();
    tmp$ = descriptor.kind;
    if (equals(tmp$, StructureKind.LIST) || Kotlin.isType(tmp$, PolymorphicKind)) {
      var tmp$_1 = this.json;
      if (!Kotlin.isType(currentObject, JsonArray)) {
        throw JsonDecodingException_0(-1, 'Expected ' + getKClass(JsonArray) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(currentObject));
      }tmp$_0 = new JsonTreeListDecoder(tmp$_1, currentObject);
    } else if (equals(tmp$, StructureKind.MAP)) {
      var $receiver = this.json;
      var tmp$_2;
      var keyDescriptor = get_carrierDescriptor(descriptor.getElementDescriptor_za3lpa$(0));
      var keyKind = keyDescriptor.kind;
      if (Kotlin.isType(keyKind, PrimitiveKind) || equals(keyKind, SerialKind.ENUM)) {
        var tmp$_3 = this.json;
        if (!Kotlin.isType(currentObject, JsonObject)) {
          throw JsonDecodingException_0(-1, 'Expected ' + getKClass(JsonObject) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(currentObject));
        }tmp$_2 = new JsonTreeMapDecoder(tmp$_3, currentObject);
      } else if ($receiver.configuration.allowStructuredMapKeys) {
        var tmp$_4 = this.json;
        if (!Kotlin.isType(currentObject, JsonArray)) {
          throw JsonDecodingException_0(-1, 'Expected ' + getKClass(JsonArray) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(currentObject));
        }tmp$_2 = new JsonTreeListDecoder(tmp$_4, currentObject);
      } else {
        throw InvalidKeyKindException(keyDescriptor);
      }
      tmp$_0 = tmp$_2;
    } else {
      var tmp$_5 = this.json;
      if (!Kotlin.isType(currentObject, JsonObject)) {
        throw JsonDecodingException_0(-1, 'Expected ' + getKClass(JsonObject) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(currentObject));
      }tmp$_0 = new JsonTreeDecoder(tmp$_5, currentObject);
    }
    return tmp$_0;
  };
  AbstractJsonTreeDecoder.prototype.endStructure_24f42q$ = function (descriptor) {
  };
  AbstractJsonTreeDecoder.prototype.decodeNotNullMark = function () {
    return !Kotlin.isType(this.currentObject_0(), JsonNull);
  };
  AbstractJsonTreeDecoder.prototype.getValue_61zpoe$ = function (tag) {
    var tmp$, tmp$_0;
    var currentElement = this.currentElement_61zpoe$(tag);
    tmp$_0 = Kotlin.isType(tmp$ = currentElement, JsonPrimitive) ? tmp$ : null;
    if (tmp$_0 == null) {
      throw JsonDecodingException_1(-1, 'Expected JsonPrimitive at ' + tag + ', found ' + currentElement, this.currentObject_0().toString());
    }return tmp$_0;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedEnum_gaombr$ = function (tag, enumDescriptor) {
    return getJsonNameIndexOrThrow(enumDescriptor, this.json, this.getValue_61zpoe$(tag).content);
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedNull_11rb$ = function (tag) {
    return null;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedNotNullMark_11rb$ = function (tag) {
    return this.currentElement_61zpoe$(tag) !== JsonNull_getInstance();
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedBoolean_11rb$ = function (tag) {
    var tmp$;
    var value = this.getValue_61zpoe$(tag);
    if (!this.json.configuration.isLenient) {
      var literal = Kotlin.isType(tmp$ = value, JsonLiteral) ? tmp$ : throwCCE();
      if (literal.isString)
        throw JsonDecodingException_1(-1, "Boolean literal for key '" + tag + "' should be unquoted." + '\n' + lenientHint, this.currentObject_0().toString());
    }var primitive_0$result;
    var tmp$_0;
    try {
      var tmp$_1;
      tmp$_1 = get_booleanOrNull(value);
      if (tmp$_1 == null) {
        throw IllegalArgumentException_init_0();
      }primitive_0$result = (tmp$_0 = tmp$_1) != null ? tmp$_0 : this.unparsedPrimitive_0('boolean');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('boolean');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedByte_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      var result = get_int($receiver);
      primitive_0$result = (tmp$ = (new IntRange(kotlin_js_internal_ByteCompanionObject.MIN_VALUE, kotlin_js_internal_ByteCompanionObject.MAX_VALUE)).contains_mef7kx$(result) ? toByte(result) : null) != null ? tmp$ : this.unparsedPrimitive_0('byte');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('byte');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedShort_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      var result = get_int($receiver);
      primitive_0$result = (tmp$ = (new IntRange(kotlin_js_internal_ShortCompanionObject.MIN_VALUE, kotlin_js_internal_ShortCompanionObject.MAX_VALUE)).contains_mef7kx$(result) ? toShort(result) : null) != null ? tmp$ : this.unparsedPrimitive_0('short');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('short');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedInt_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      primitive_0$result = (tmp$ = get_int($receiver)) != null ? tmp$ : this.unparsedPrimitive_0('int');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('int');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedLong_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      primitive_0$result = (tmp$ = get_long($receiver)) != null ? tmp$ : this.unparsedPrimitive_0('long');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('long');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedFloat_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      primitive_0$result = (tmp$ = get_float($receiver)) != null ? tmp$ : this.unparsedPrimitive_0('float');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('float');
      } else
        throw e;
    }
    var result = primitive_0$result;
    var specialFp = this.json.configuration.allowSpecialFloatingPointValues;
    if (specialFp || isFinite(result))
      return result;
    throw InvalidFloatingPointDecoded(result, tag, this.currentObject_0().toString());
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedDouble_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      primitive_0$result = (tmp$ = get_double($receiver)) != null ? tmp$ : this.unparsedPrimitive_0('double');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('double');
      } else
        throw e;
    }
    var result = primitive_0$result;
    var specialFp = this.json.configuration.allowSpecialFloatingPointValues;
    if (specialFp || isFinite_0(result))
      return result;
    throw InvalidFloatingPointDecoded(result, tag, this.currentObject_0().toString());
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedChar_11rb$ = function (tag) {
    var $receiver = this.getValue_61zpoe$(tag);
    var primitive_0$result;
    var tmp$;
    try {
      primitive_0$result = (tmp$ = toBoxedChar(single($receiver.content))) != null ? tmp$ : this.unparsedPrimitive_0('char');
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0('char');
      } else
        throw e;
    }
    return primitive_0$result;
  };
  AbstractJsonTreeDecoder.prototype.primitive_0 = function ($receiver, primitive, block) {
    var tmp$;
    try {
      return (tmp$ = block($receiver)) != null ? tmp$ : this.unparsedPrimitive_0(primitive);
    } catch (e) {
      if (Kotlin.isType(e, IllegalArgumentException)) {
        this.unparsedPrimitive_0(primitive);
      } else
        throw e;
    }
  };
  AbstractJsonTreeDecoder.prototype.unparsedPrimitive_0 = function (primitive) {
    throw JsonDecodingException_1(-1, "Failed to parse '" + primitive + "'", this.currentObject_0().toString());
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedString_11rb$ = function (tag) {
    var tmp$;
    var value = this.getValue_61zpoe$(tag);
    if (!this.json.configuration.isLenient) {
      var literal = Kotlin.isType(tmp$ = value, JsonLiteral) ? tmp$ : throwCCE();
      if (!literal.isString)
        throw JsonDecodingException_1(-1, "String literal for key '" + tag + "' should be quoted." + '\n' + lenientHint, this.currentObject_0().toString());
    }return value.content;
  };
  AbstractJsonTreeDecoder.prototype.decodeTaggedInline_gaombr$ = function (tag, inlineDescriptor) {
    return get_isUnsignedNumber(inlineDescriptor) ? new JsonDecoderForUnsignedTypes(new JsonLexer(this.getValue_61zpoe$(tag).content), this.json) : NamedValueDecoder.prototype.decodeTaggedInline_gaombr$.call(this, tag, inlineDescriptor);
  };
  AbstractJsonTreeDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AbstractJsonTreeDecoder',
    interfaces: [JsonDecoder, NamedValueDecoder]
  };
  function JsonPrimitiveDecoder(json, value) {
    AbstractJsonTreeDecoder.call(this, json, value);
    this.value_icegni$_0 = value;
    this.pushTag_11rb$(PRIMITIVE_TAG);
  }
  Object.defineProperty(JsonPrimitiveDecoder.prototype, 'value', {
    get: function () {
      return this.value_icegni$_0;
    }
  });
  JsonPrimitiveDecoder.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    return 0;
  };
  JsonPrimitiveDecoder.prototype.currentElement_61zpoe$ = function (tag) {
    if (!(tag === PRIMITIVE_TAG)) {
      var message = "This input can only handle primitives with 'primitive' tag";
      throw IllegalArgumentException_init(message.toString());
    }return this.value;
  };
  JsonPrimitiveDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonPrimitiveDecoder',
    interfaces: [AbstractJsonTreeDecoder]
  };
  function JsonTreeDecoder(json, value, polyDiscriminator, polyDescriptor) {
    if (polyDiscriminator === void 0)
      polyDiscriminator = null;
    if (polyDescriptor === void 0)
      polyDescriptor = null;
    AbstractJsonTreeDecoder.call(this, json, value);
    this.value_nyd9q1$_0 = value;
    this.polyDiscriminator_0 = polyDiscriminator;
    this.polyDescriptor_0 = polyDescriptor;
    this.position_0 = 0;
  }
  Object.defineProperty(JsonTreeDecoder.prototype, 'value', {
    get: function () {
      return this.value_nyd9q1$_0;
    }
  });
  JsonTreeDecoder.prototype.coerceInputValue_0 = function (descriptor, index, tag) {
    var tmp$, tmp$_0, tmp$_1;
    var elementDescriptor = descriptor.getElementDescriptor_za3lpa$(index);
    if (Kotlin.isType(this.currentElement_61zpoe$(tag), JsonNull) && !elementDescriptor.isNullable)
      return true;
    if (equals(elementDescriptor.kind, SerialKind.ENUM)) {
      tmp$_1 = (tmp$_0 = Kotlin.isType(tmp$ = this.currentElement_61zpoe$(tag), JsonPrimitive) ? tmp$ : null) != null ? get_contentOrNull(tmp$_0) : null;
      if (tmp$_1 == null) {
        return false;
      }var enumValue = tmp$_1;
      var enumIndex = getJsonNameIndex(elementDescriptor, this.json, enumValue);
      if (enumIndex === -3)
        return true;
    }return false;
  };
  JsonTreeDecoder.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    var tmp$;
    while (this.position_0 < descriptor.elementsCount) {
      var name = this.getTag_av9bu7$(descriptor, (tmp$ = this.position_0, this.position_0 = tmp$ + 1 | 0, tmp$));
      var $receiver = this.value;
      var tmp$_0;
      if ((Kotlin.isType(tmp$_0 = $receiver, Map) ? tmp$_0 : throwCCE()).containsKey_11rb$(name) && (!this.configuration_0.coerceInputValues || !this.coerceInputValue_0(descriptor, this.position_0 - 1 | 0, name))) {
        return this.position_0 - 1 | 0;
      }}
    return -1;
  };
  JsonTreeDecoder.prototype.elementName_szpzho$ = function (desc, index) {
    var mainName = desc.getElementName_za3lpa$(index);
    if (!this.configuration_0.useAlternativeNames)
      return mainName;
    if (this.value.keys.contains_11rb$(mainName))
      return mainName;
    var alternativeNamesMap = this.json.schemaCache_8be2vx$.getOrPut_a5unbe$(desc, JsonAlternativeNamesKey, getCallableRef('buildAlternativeNamesMap', function ($receiver) {
      return buildAlternativeNamesMap($receiver);
    }.bind(null, desc)));
    var $receiver = this.value.keys;
    var firstOrNull$result;
    firstOrNull$break: do {
      var tmp$;
      tmp$ = $receiver.iterator();
      while (tmp$.hasNext()) {
        var element = tmp$.next();
        if (alternativeNamesMap.get_11rb$(element) === index) {
          firstOrNull$result = element;
          break firstOrNull$break;
        }}
      firstOrNull$result = null;
    }
     while (false);
    var nameInObject = firstOrNull$result;
    return nameInObject != null ? nameInObject : mainName;
  };
  JsonTreeDecoder.prototype.currentElement_61zpoe$ = function (tag) {
    return getValue(this.value, tag);
  };
  JsonTreeDecoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    if (descriptor === this.polyDescriptor_0)
      return this;
    return AbstractJsonTreeDecoder.prototype.beginStructure_24f42q$.call(this, descriptor);
  };
  JsonTreeDecoder.prototype.endStructure_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    if (this.configuration_0.ignoreUnknownKeys || Kotlin.isType(descriptor.kind, PolymorphicKind))
      return;
    if (!this.configuration_0.useAlternativeNames)
      tmp$_1 = jsonCachedSerialNames(descriptor);
    else {
      tmp$_0 = jsonCachedSerialNames(descriptor);
      var $receiver = (tmp$ = this.json.schemaCache_8be2vx$.get_khmtuq$(descriptor, JsonAlternativeNamesKey)) != null ? tmp$.keys : null;
      tmp$_1 = plus(tmp$_0, $receiver != null ? $receiver : emptySet());
    }
    var names = tmp$_1;
    tmp$_2 = this.value.keys.iterator();
    while (tmp$_2.hasNext()) {
      var key = tmp$_2.next();
      if (!names.contains_11rb$(key) && !equals(key, this.polyDiscriminator_0)) {
        throw UnknownKeyException(key, this.value.toString());
      }}
  };
  JsonTreeDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeDecoder',
    interfaces: [AbstractJsonTreeDecoder]
  };
  function JsonTreeMapDecoder(json, value) {
    JsonTreeDecoder.call(this, json, value);
    this.value_ozc1lj$_0 = value;
    this.keys_0 = toList(this.value.keys);
    this.size_0 = this.keys_0.size * 2 | 0;
    this.position_1 = -1;
  }
  Object.defineProperty(JsonTreeMapDecoder.prototype, 'value', {
    get: function () {
      return this.value_ozc1lj$_0;
    }
  });
  JsonTreeMapDecoder.prototype.elementName_szpzho$ = function (desc, index) {
    var i = index / 2 | 0;
    return this.keys_0.get_za3lpa$(i);
  };
  JsonTreeMapDecoder.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    while (this.position_1 < (this.size_0 - 1 | 0)) {
      this.position_1 = this.position_1 + 1 | 0;
      return this.position_1;
    }
    return -1;
  };
  JsonTreeMapDecoder.prototype.currentElement_61zpoe$ = function (tag) {
    return this.position_1 % 2 === 0 ? JsonPrimitive_2(tag) : getValue(this.value, tag);
  };
  JsonTreeMapDecoder.prototype.endStructure_24f42q$ = function (descriptor) {
  };
  JsonTreeMapDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeMapDecoder',
    interfaces: [JsonTreeDecoder]
  };
  function JsonTreeListDecoder(json, value) {
    AbstractJsonTreeDecoder.call(this, json, value);
    this.value_z81zg7$_0 = value;
    this.size_0 = this.value.size;
    this.currentIndex_0 = -1;
  }
  Object.defineProperty(JsonTreeListDecoder.prototype, 'value', {
    get: function () {
      return this.value_z81zg7$_0;
    }
  });
  JsonTreeListDecoder.prototype.elementName_szpzho$ = function (desc, index) {
    return index.toString();
  };
  JsonTreeListDecoder.prototype.currentElement_61zpoe$ = function (tag) {
    return this.value.get_za3lpa$(toInt(tag));
  };
  JsonTreeListDecoder.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    while (this.currentIndex_0 < (this.size_0 - 1 | 0)) {
      this.currentIndex_0 = this.currentIndex_0 + 1 | 0;
      return this.currentIndex_0;
    }
    return -1;
  };
  JsonTreeListDecoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeListDecoder',
    interfaces: [AbstractJsonTreeDecoder]
  };
  function writeJson$lambda(closure$result) {
    return function (it) {
      closure$result.v = it;
      return Unit;
    };
  }
  function writeJson($receiver, value, serializer) {
    var result = {v: null};
    var encoder = new JsonTreeEncoder($receiver, writeJson$lambda(result));
    encoder.encodeSerializableValue_tf03ej$(serializer, value);
    return result.v == null ? throwUPAE('result') : result.v;
  }
  function AbstractJsonTreeEncoder(json, nodeConsumer) {
    NamedValueEncoder.call(this);
    this.json_927t60$_0 = json;
    this.nodeConsumer_0 = nodeConsumer;
    this.configuration_0 = this.json.configuration;
    this.writePolymorphic_0 = false;
  }
  Object.defineProperty(AbstractJsonTreeEncoder.prototype, 'json', {
    get: function () {
      return this.json_927t60$_0;
    }
  });
  Object.defineProperty(AbstractJsonTreeEncoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.json.serializersModule;
    }
  });
  AbstractJsonTreeEncoder.prototype.encodeJsonElement_qiw0cd$ = function (element) {
    this.encodeSerializableValue_tf03ej$(JsonElementSerializer_getInstance(), element);
  };
  AbstractJsonTreeEncoder.prototype.shouldEncodeElementDefault_szpzho$ = function (descriptor, index) {
    return this.configuration_0.encodeDefaults;
  };
  AbstractJsonTreeEncoder.prototype.composeName_puj7f4$ = function (parentName, childName) {
    return childName;
  };
  AbstractJsonTreeEncoder.prototype.encodeNull = function () {
    var tmp$;
    tmp$ = this.currentTagOrNull;
    if (tmp$ == null) {
      return this.nodeConsumer_0(JsonNull_getInstance());
    }var tag = tmp$;
    this.encodeTaggedNull_11rb$(tag);
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedNull_11rb$ = function (tag) {
    this.putElement_zafu29$(tag, JsonNull_getInstance());
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedInt_dpg1yx$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedByte_19qe40$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedShort_veccj0$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedLong_19wkf8$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedFloat_vlf4p8$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
    if (!this.configuration_0.allowSpecialFloatingPointValues && !isFinite(value)) {
      throw InvalidFloatingPointEncoded_0(value, tag, this.getCurrent().toString());
    }};
  AbstractJsonTreeEncoder.prototype.encodeSerializableValue_tf03ej$ = function (serializer, value) {
    if (this.currentTagOrNull != null || (!Kotlin.isType(serializer.descriptor.kind, PrimitiveKind) && serializer.descriptor.kind !== SerialKind.ENUM)) {
      encodePolymorphically$break: do {
        var tmp$, tmp$_0;
        if (!Kotlin.isType(serializer, AbstractPolymorphicSerializer) || this.json.configuration.useArrayPolymorphism) {
          serializer.serialize_55azsf$(this, value);
          break encodePolymorphically$break;
        }var actualSerializer = findActualSerializer(this, Kotlin.isType(tmp$ = serializer, SerializationStrategy) ? tmp$ : throwCCE(), Kotlin.isType(tmp$_0 = value, Any) ? tmp$_0 : throwCCE());
        this.writePolymorphic_0 = true;
        actualSerializer.serialize_55azsf$(this, value);
      }
       while (false);
    } else {
      var $receiver = new JsonPrimitiveEncoder(this.json, this.nodeConsumer_0);
      $receiver.encodeSerializableValue_tf03ej$(serializer, value);
      $receiver.endEncode_24f42q$(serializer.descriptor);
    }
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedDouble_e37ph5$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_1(value));
    if (!this.configuration_0.allowSpecialFloatingPointValues && !isFinite_0(value)) {
      throw InvalidFloatingPointEncoded_0(value, tag, this.getCurrent().toString());
    }};
  AbstractJsonTreeEncoder.prototype.encodeTaggedBoolean_iuyhfk$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_0(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedChar_19qo1q$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_2(String.fromCharCode(value)));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedString_l9l8mx$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_2(value));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedEnum_4xdzqf$ = function (tag, enumDescriptor, ordinal) {
    this.putElement_zafu29$(tag, JsonPrimitive_2(enumDescriptor.getElementName_za3lpa$(ordinal)));
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedValue_dpg7wc$ = function (tag, value) {
    this.putElement_zafu29$(tag, JsonPrimitive_2(value.toString()));
  };
  function AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral(closure$tag, this$AbstractJsonTreeEncoder) {
    this.closure$tag = closure$tag;
    this.this$AbstractJsonTreeEncoder = this$AbstractJsonTreeEncoder;
    AbstractEncoder.call(this);
    this.serializersModule_tav261$_0 = this$AbstractJsonTreeEncoder.json.serializersModule;
  }
  Object.defineProperty(AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.serializersModule_tav261$_0;
    }
  });
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.putUnquotedString_61zpoe$ = function (s) {
    this.this$AbstractJsonTreeEncoder.putElement_zafu29$(this.closure$tag, new JsonLiteral(s, false));
  };
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.encodeInt_za3lpa$ = function (value) {
    this.putUnquotedString_61zpoe$((new UInt_init(value)).toString());
  };
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.encodeLong_s8cxhz$ = function (value) {
    this.putUnquotedString_61zpoe$((new ULong_init(value)).toString());
  };
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.encodeByte_s8j3t7$ = function (value) {
    this.putUnquotedString_61zpoe$((new UByte_init(value)).toString());
  };
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.prototype.encodeShort_mq22fl$ = function (value) {
    this.putUnquotedString_61zpoe$((new UShort_init(value)).toString());
  };
  AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [AbstractEncoder]
  };
  AbstractJsonTreeEncoder.prototype.encodeTaggedInline_gaombr$ = function (tag, inlineDescriptor) {
    return get_isUnsignedNumber(inlineDescriptor) ? new AbstractJsonTreeEncoder$encodeTaggedInline$ObjectLiteral(tag, this) : NamedValueEncoder.prototype.encodeTaggedInline_gaombr$.call(this, tag, inlineDescriptor);
  };
  function AbstractJsonTreeEncoder$beginStructure$lambda(this$AbstractJsonTreeEncoder) {
    return function (node) {
      this$AbstractJsonTreeEncoder.putElement_zafu29$(this$AbstractJsonTreeEncoder.currentTag, node);
      return Unit;
    };
  }
  AbstractJsonTreeEncoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0;
    var consumer = this.currentTagOrNull == null ? this.nodeConsumer_0 : AbstractJsonTreeEncoder$beginStructure$lambda(this);
    tmp$ = descriptor.kind;
    if (equals(tmp$, StructureKind.LIST) || Kotlin.isType(tmp$, PolymorphicKind))
      tmp$_0 = new JsonTreeListEncoder(this.json, consumer);
    else if (equals(tmp$, StructureKind.MAP)) {
      var $receiver = this.json;
      var tmp$_1;
      var keyDescriptor = get_carrierDescriptor(descriptor.getElementDescriptor_za3lpa$(0));
      var keyKind = keyDescriptor.kind;
      if (Kotlin.isType(keyKind, PrimitiveKind) || equals(keyKind, SerialKind.ENUM)) {
        tmp$_1 = new JsonTreeMapEncoder(this.json, consumer);
      } else if ($receiver.configuration.allowStructuredMapKeys) {
        tmp$_1 = new JsonTreeListEncoder(this.json, consumer);
      } else {
        throw InvalidKeyKindException(keyDescriptor);
      }
      tmp$_0 = tmp$_1;
    } else
      tmp$_0 = new JsonTreeEncoder(this.json, consumer);
    var encoder = tmp$_0;
    if (this.writePolymorphic_0) {
      this.writePolymorphic_0 = false;
      encoder.putElement_zafu29$(this.configuration_0.classDiscriminator, JsonPrimitive_2(descriptor.serialName));
    }return encoder;
  };
  AbstractJsonTreeEncoder.prototype.endEncode_24f42q$ = function (descriptor) {
    this.nodeConsumer_0(this.getCurrent());
  };
  AbstractJsonTreeEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'AbstractJsonTreeEncoder',
    interfaces: [JsonEncoder, NamedValueEncoder]
  };
  var PRIMITIVE_TAG;
  function JsonPrimitiveEncoder(json, nodeConsumer) {
    AbstractJsonTreeEncoder.call(this, json, nodeConsumer);
    this.content_0 = null;
    this.pushTag_11rb$(PRIMITIVE_TAG);
  }
  JsonPrimitiveEncoder.prototype.putElement_zafu29$ = function (key, element) {
    if (!(key === PRIMITIVE_TAG)) {
      var message = "This output can only consume primitives with 'primitive' tag";
      throw IllegalArgumentException_init(message.toString());
    }if (!(this.content_0 == null)) {
      var message_0 = 'Primitive element was already recorded. Does call to .encodeXxx happen more than once?';
      throw IllegalArgumentException_init(message_0.toString());
    }this.content_0 = element;
  };
  JsonPrimitiveEncoder.prototype.getCurrent = function () {
    var value = this.content_0;
    var requireNotNull$result;
    if (value == null) {
      var message = 'Primitive element has not been recorded. Is call to .encodeXxx is missing in serializer?';
      throw IllegalArgumentException_init(message.toString());
    } else {
      requireNotNull$result = value;
    }
    return requireNotNull$result;
  };
  JsonPrimitiveEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonPrimitiveEncoder',
    interfaces: [AbstractJsonTreeEncoder]
  };
  function JsonTreeEncoder(json, nodeConsumer) {
    AbstractJsonTreeEncoder.call(this, json, nodeConsumer);
    this.content_0 = LinkedHashMap_init();
  }
  JsonTreeEncoder.prototype.putElement_zafu29$ = function (key, element) {
    this.content_0.put_xwzc9p$(key, element);
  };
  JsonTreeEncoder.prototype.getCurrent = function () {
    return new JsonObject(this.content_0);
  };
  JsonTreeEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeEncoder',
    interfaces: [AbstractJsonTreeEncoder]
  };
  function JsonTreeMapEncoder(json, nodeConsumer) {
    JsonTreeEncoder.call(this, json, nodeConsumer);
    this.tag_s622uw$_0 = this.tag_s622uw$_0;
    this.isKey_0 = true;
  }
  Object.defineProperty(JsonTreeMapEncoder.prototype, 'tag_0', {
    configurable: true,
    get: function () {
      if (this.tag_s622uw$_0 == null)
        return throwUPAE('tag');
      return this.tag_s622uw$_0;
    },
    set: function (tag) {
      this.tag_s622uw$_0 = tag;
    }
  });
  JsonTreeMapEncoder.prototype.putElement_zafu29$ = function (key, element) {
    var tmp$;
    if (this.isKey_0) {
      if (Kotlin.isType(element, JsonPrimitive))
        tmp$ = element.content;
      else if (Kotlin.isType(element, JsonObject))
        throw InvalidKeyKindException(JsonObjectSerializer_getInstance().descriptor);
      else if (Kotlin.isType(element, JsonArray))
        throw InvalidKeyKindException(JsonArraySerializer_getInstance().descriptor);
      else
        tmp$ = Kotlin.noWhenBranchMatched();
      this.tag_0 = tmp$;
      this.isKey_0 = false;
    } else {
      var $receiver = this.content_0;
      var key_0 = this.tag_0;
      $receiver.put_xwzc9p$(key_0, element);
      this.isKey_0 = true;
    }
  };
  JsonTreeMapEncoder.prototype.getCurrent = function () {
    return new JsonObject(this.content_0);
  };
  JsonTreeMapEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeMapEncoder',
    interfaces: [JsonTreeEncoder]
  };
  function JsonTreeListEncoder(json, nodeConsumer) {
    AbstractJsonTreeEncoder.call(this, json, nodeConsumer);
    this.array_0 = ArrayList_init();
  }
  JsonTreeListEncoder.prototype.elementName_szpzho$ = function (descriptor, index) {
    return index.toString();
  };
  JsonTreeListEncoder.prototype.putElement_zafu29$ = function (key, element) {
    var idx = toInt(key);
    this.array_0.add_wxm5ur$(idx, element);
  };
  JsonTreeListEncoder.prototype.getCurrent = function () {
    return new JsonArray(this.array_0);
  };
  JsonTreeListEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTreeListEncoder',
    interfaces: [AbstractJsonTreeEncoder]
  };
  var cast = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.internal.cast_f6pxlz$', wrapFunction(function () {
    var getKClass = Kotlin.getKClass;
    var JsonDecodingException = _.kotlinx.serialization.json.internal.JsonDecodingException_f0n09d$;
    return function (T_0, isT, value, descriptor) {
      if (!isT(value)) {
        throw JsonDecodingException(-1, 'Expected ' + getKClass(T_0) + ' as the serialized body of ' + descriptor.serialName + ', but had ' + Kotlin.getKClassFromExpression(value));
      }return value;
    };
  }));
  function WriteMode(name, ordinal, begin, end) {
    Enum.call(this);
    this.begin = toBoxedChar(begin);
    this.end = toBoxedChar(end);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function WriteMode_initFields() {
    WriteMode_initFields = function () {
    };
    WriteMode$OBJ_instance = new WriteMode('OBJ', 0, BEGIN_OBJ, END_OBJ);
    WriteMode$LIST_instance = new WriteMode('LIST', 1, BEGIN_LIST, END_LIST);
    WriteMode$MAP_instance = new WriteMode('MAP', 2, BEGIN_OBJ, END_OBJ);
    WriteMode$POLY_OBJ_instance = new WriteMode('POLY_OBJ', 3, BEGIN_LIST, END_LIST);
  }
  var WriteMode$OBJ_instance;
  function WriteMode$OBJ_getInstance() {
    WriteMode_initFields();
    return WriteMode$OBJ_instance;
  }
  var WriteMode$LIST_instance;
  function WriteMode$LIST_getInstance() {
    WriteMode_initFields();
    return WriteMode$LIST_instance;
  }
  var WriteMode$MAP_instance;
  function WriteMode$MAP_getInstance() {
    WriteMode_initFields();
    return WriteMode$MAP_instance;
  }
  var WriteMode$POLY_OBJ_instance;
  function WriteMode$POLY_OBJ_getInstance() {
    WriteMode_initFields();
    return WriteMode$POLY_OBJ_instance;
  }
  WriteMode.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'WriteMode',
    interfaces: [Enum]
  };
  function WriteMode$values() {
    return [WriteMode$OBJ_getInstance(), WriteMode$LIST_getInstance(), WriteMode$MAP_getInstance(), WriteMode$POLY_OBJ_getInstance()];
  }
  WriteMode.values = WriteMode$values;
  function WriteMode$valueOf(name) {
    switch (name) {
      case 'OBJ':
        return WriteMode$OBJ_getInstance();
      case 'LIST':
        return WriteMode$LIST_getInstance();
      case 'MAP':
        return WriteMode$MAP_getInstance();
      case 'POLY_OBJ':
        return WriteMode$POLY_OBJ_getInstance();
      default:throwISE('No enum constant kotlinx.serialization.json.internal.WriteMode.' + name);
    }
  }
  WriteMode.valueOf_61zpoe$ = WriteMode$valueOf;
  function switchMode($receiver, desc) {
    var tmp$;
    tmp$ = desc.kind;
    if (Kotlin.isType(tmp$, PolymorphicKind))
      return WriteMode$POLY_OBJ_getInstance();
    else if (equals(tmp$, StructureKind.LIST))
      return WriteMode$LIST_getInstance();
    else if (equals(tmp$, StructureKind.MAP)) {
      var tmp$_0;
      var keyDescriptor = get_carrierDescriptor(desc.getElementDescriptor_za3lpa$(0));
      var keyKind = keyDescriptor.kind;
      if (Kotlin.isType(keyKind, PrimitiveKind) || equals(keyKind, SerialKind.ENUM)) {
        tmp$_0 = WriteMode$MAP_getInstance();
      } else if ($receiver.configuration.allowStructuredMapKeys) {
        tmp$_0 = WriteMode$LIST_getInstance();
      } else {
        throw InvalidKeyKindException(keyDescriptor);
      }
      return tmp$_0;
    } else
      return WriteMode$OBJ_getInstance();
  }
  var selectMapMode = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.internal.selectMapMode_clqyd9$', wrapFunction(function () {
    var get_carrierDescriptor = _.kotlinx.serialization.json.internal.get_carrierDescriptor_tie8r4$;
    var PrimitiveKind = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.descriptors.PrimitiveKind;
    var SerialKind = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.descriptors.SerialKind;
    var equals = Kotlin.equals;
    var InvalidKeyKindException = _.kotlinx.serialization.json.internal.InvalidKeyKindException_jo479d$;
    return function ($receiver, mapDescriptor, ifMap, ifList) {
      var tmp$;
      var keyDescriptor = get_carrierDescriptor(mapDescriptor.getElementDescriptor_za3lpa$(0));
      var keyKind = keyDescriptor.kind;
      if (Kotlin.isType(keyKind, PrimitiveKind) || equals(keyKind, SerialKind.ENUM)) {
        tmp$ = ifMap();
      } else if ($receiver.configuration.allowStructuredMapKeys) {
        tmp$ = ifList();
      } else {
        throw InvalidKeyKindException(keyDescriptor);
      }
      return tmp$;
    };
  }));
  function get_carrierDescriptor($receiver) {
    return $receiver.isInline ? $receiver.getElementDescriptor_za3lpa$(0) : $receiver;
  }
  function Json(configuration, serializersModule) {
    Json$Default_getInstance();
    this.configuration = configuration;
    this.serializersModule_vis8y$_0 = serializersModule;
    this.schemaCache_8be2vx$ = new DescriptorSchemaCache();
  }
  Object.defineProperty(Json.prototype, 'serializersModule', {
    get: function () {
      return this.serializersModule_vis8y$_0;
    }
  });
  function Json$Default() {
    Json$Default_instance = this;
    Json.call(this, new JsonConfiguration(), modules.EmptySerializersModule);
  }
  Json$Default.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Default',
    interfaces: [Json]
  };
  var Json$Default_instance = null;
  function Json$Default_getInstance() {
    if (Json$Default_instance === null) {
      new Json$Default();
    }return Json$Default_instance;
  }
  Json.prototype.encodeToString_tf03ej$ = function (serializer, value) {
    var result = new JsonStringBuilder();
    try {
      var encoder = StreamingJsonEncoder_init(result, this, WriteMode$OBJ_getInstance(), Kotlin.newArray(WriteMode$values().length, null));
      encoder.encodeSerializableValue_tf03ej$(serializer, value);
      return result.toString();
    }finally {
      result.release();
    }
  };
  Json.prototype.decodeFromString_awif5v$ = function (deserializer, string) {
    var lexer = new JsonLexer(string);
    var input = new StreamingJsonDecoder(this, WriteMode$OBJ_getInstance(), lexer);
    var result = input.decodeSerializableValue_w63s0f$(deserializer);
    lexer.expectEof();
    return result;
  };
  Json.prototype.encodeToJsonElement_tf03ej$ = function (serializer, value) {
    return writeJson(this, value, serializer);
  };
  Json.prototype.decodeFromJsonElement_htt2tq$ = function (deserializer, element) {
    return readJson(this, element, deserializer);
  };
  Json.prototype.parseToJsonElement_61zpoe$ = function (string) {
    return this.decodeFromString_awif5v$(JsonElementSerializer_getInstance(), string);
  };
  Json.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Json',
    interfaces: [StringFormat]
  };
  function Json_0(from, builderAction) {
    if (from === void 0)
      from = Json$Default_getInstance();
    var builder = new JsonBuilder(from);
    builderAction(builder);
    var conf = builder.build_8be2vx$();
    return new JsonImpl(conf, builder.serializersModule);
  }
  var encodeToJsonElement = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.encodeToJsonElement_eahrye$', wrapFunction(function () {
    var getReifiedTypeParameterKType = Kotlin.getReifiedTypeParameterKType;
    var serializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.serializer_ca95z9$;
    var KSerializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.KSerializer;
    var throwCCE = Kotlin.throwCCE;
    return function (T_0, isT, $receiver, value) {
      var $receiver_0 = $receiver.serializersModule;
      var tmp$;
      return $receiver.encodeToJsonElement_tf03ej$(Kotlin.isType(tmp$ = serializer($receiver_0, getReifiedTypeParameterKType(T_0)), KSerializer) ? tmp$ : throwCCE(), value);
    };
  }));
  var decodeFromJsonElement = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.decodeFromJsonElement_cmu2rn$', wrapFunction(function () {
    var getReifiedTypeParameterKType = Kotlin.getReifiedTypeParameterKType;
    var serializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.serializer_ca95z9$;
    var KSerializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.KSerializer;
    var throwCCE = Kotlin.throwCCE;
    return function (T_0, isT, $receiver, json) {
      var $receiver_0 = $receiver.serializersModule;
      var tmp$;
      return $receiver.decodeFromJsonElement_htt2tq$(Kotlin.isType(tmp$ = serializer($receiver_0, getReifiedTypeParameterKType(T_0)), KSerializer) ? tmp$ : throwCCE(), json);
    };
  }));
  function JsonBuilder(json) {
    this.encodeDefaults = json.configuration.encodeDefaults;
    this.ignoreUnknownKeys = json.configuration.ignoreUnknownKeys;
    this.isLenient = json.configuration.isLenient;
    this.allowStructuredMapKeys = json.configuration.allowStructuredMapKeys;
    this.prettyPrint = json.configuration.prettyPrint;
    this.prettyPrintIndent = json.configuration.prettyPrintIndent;
    this.coerceInputValues = json.configuration.coerceInputValues;
    this.useArrayPolymorphism = json.configuration.useArrayPolymorphism;
    this.classDiscriminator = json.configuration.classDiscriminator;
    this.allowSpecialFloatingPointValues = json.configuration.allowSpecialFloatingPointValues;
    this.useAlternativeNames = json.configuration.useAlternativeNames;
    this.serializersModule = json.serializersModule;
  }
  JsonBuilder.prototype.build_8be2vx$ = function () {
    if (this.useArrayPolymorphism) {
      if (!equals(this.classDiscriminator, defaultDiscriminator)) {
        var message = 'Class discriminator should not be specified when array polymorphism is specified';
        throw IllegalArgumentException_init(message.toString());
      }}if (!this.prettyPrint) {
      if (!equals(this.prettyPrintIndent, defaultIndent)) {
        var message_0 = 'Indent should not be specified when default printing mode is used';
        throw IllegalArgumentException_init(message_0.toString());
      }} else if (!equals(this.prettyPrintIndent, defaultIndent)) {
      var $receiver = this.prettyPrintIndent;
      var all$result;
      all$break: do {
        var tmp$;
        tmp$ = iterator($receiver);
        while (tmp$.hasNext()) {
          var element = unboxChar(tmp$.next());
          var it = toBoxedChar(element);
          if (!(unboxChar(it) === 32 || unboxChar(it) === 9 || unboxChar(it) === 13 || unboxChar(it) === 10)) {
            all$result = false;
            break all$break;
          }}
        all$result = true;
      }
       while (false);
      var allWhitespaces = all$result;
      if (!allWhitespaces) {
        var message_1 = 'Only whitespace, tab, newline and carriage return are allowed as pretty print symbols. Had ' + this.prettyPrintIndent;
        throw IllegalArgumentException_init(message_1.toString());
      }}return new JsonConfiguration(this.encodeDefaults, this.ignoreUnknownKeys, this.isLenient, this.allowStructuredMapKeys, this.prettyPrint, this.prettyPrintIndent, this.coerceInputValues, this.useArrayPolymorphism, this.classDiscriminator, this.allowSpecialFloatingPointValues, this.useAlternativeNames);
  };
  JsonBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonBuilder',
    interfaces: []
  };
  function JsonImpl(configuration, module_0) {
    Json.call(this, configuration, module_0);
    this.validateConfiguration_0();
  }
  JsonImpl.prototype.validateConfiguration_0 = function () {
    if (equals(this.serializersModule, modules.EmptySerializersModule))
      return;
    var collector = new PolymorphismValidator(this.configuration.useArrayPolymorphism, this.configuration.classDiscriminator);
    this.serializersModule.dumpTo_ukvgvw$(collector);
  };
  JsonImpl.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonImpl',
    interfaces: [Json]
  };
  var defaultIndent;
  var defaultDiscriminator;
  function JsonConfiguration(encodeDefaults, ignoreUnknownKeys, isLenient, allowStructuredMapKeys, prettyPrint, prettyPrintIndent, coerceInputValues, useArrayPolymorphism, classDiscriminator, allowSpecialFloatingPointValues, useAlternativeNames) {
    if (encodeDefaults === void 0)
      encodeDefaults = false;
    if (ignoreUnknownKeys === void 0)
      ignoreUnknownKeys = false;
    if (isLenient === void 0)
      isLenient = false;
    if (allowStructuredMapKeys === void 0)
      allowStructuredMapKeys = false;
    if (prettyPrint === void 0)
      prettyPrint = false;
    if (prettyPrintIndent === void 0)
      prettyPrintIndent = '    ';
    if (coerceInputValues === void 0)
      coerceInputValues = false;
    if (useArrayPolymorphism === void 0)
      useArrayPolymorphism = false;
    if (classDiscriminator === void 0)
      classDiscriminator = 'type';
    if (allowSpecialFloatingPointValues === void 0)
      allowSpecialFloatingPointValues = false;
    if (useAlternativeNames === void 0)
      useAlternativeNames = true;
    this.encodeDefaults = encodeDefaults;
    this.ignoreUnknownKeys = ignoreUnknownKeys;
    this.isLenient = isLenient;
    this.allowStructuredMapKeys = allowStructuredMapKeys;
    this.prettyPrint = prettyPrint;
    this.prettyPrintIndent = prettyPrintIndent;
    this.coerceInputValues = coerceInputValues;
    this.useArrayPolymorphism = useArrayPolymorphism;
    this.classDiscriminator = classDiscriminator;
    this.allowSpecialFloatingPointValues = allowSpecialFloatingPointValues;
    this.useAlternativeNames = useAlternativeNames;
  }
  JsonConfiguration.prototype.toString = function () {
    return 'JsonConfiguration(encodeDefaults=' + this.encodeDefaults + ', ignoreUnknownKeys=' + this.ignoreUnknownKeys + ', isLenient=' + this.isLenient + ', allowStructuredMapKeys=' + this.allowStructuredMapKeys + ', prettyPrint=' + this.prettyPrint + ", prettyPrintIndent='" + this.prettyPrintIndent + "', coerceInputValues=" + this.coerceInputValues + ', useArrayPolymorphism=' + this.useArrayPolymorphism + ", classDiscriminator='" + this.classDiscriminator + "', allowSpecialFloatingPointValues=" + this.allowSpecialFloatingPointValues + ')';
  };
  JsonConfiguration.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonConfiguration',
    interfaces: []
  };
  function JsonContentPolymorphicSerializer(baseClass) {
    this.baseClass_saxo55$_0 = baseClass;
    this.descriptor_fdk25h$_0 = buildSerialDescriptor('JsonContentPolymorphicSerializer<' + toString(this.baseClass_saxo55$_0.simpleName) + '>', PolymorphicKind.SEALED, []);
  }
  Object.defineProperty(JsonContentPolymorphicSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_fdk25h$_0;
    }
  });
  JsonContentPolymorphicSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    var tmp$, tmp$_0, tmp$_1;
    var actualSerializer = (tmp$_0 = (tmp$ = encoder.serializersModule.getPolymorphic_joiicr$(this.baseClass_saxo55$_0, value)) != null ? tmp$ : serializerOrNull(Kotlin.getKClassFromExpression(value))) != null ? tmp$_0 : this.throwSubtypeNotRegistered_wimvgz$_0(Kotlin.getKClassFromExpression(value), this.baseClass_saxo55$_0);
    (Kotlin.isType(tmp$_1 = actualSerializer, KSerializer) ? tmp$_1 : throwCCE()).serialize_55azsf$(encoder, value);
  };
  JsonContentPolymorphicSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    var tmp$;
    var input = asJsonDecoder(decoder);
    var tree = input.decodeJsonElement();
    var actualSerializer = Kotlin.isType(tmp$ = this.selectDeserializer_qiw0cd$(tree), KSerializer) ? tmp$ : throwCCE();
    return input.json.decodeFromJsonElement_htt2tq$(actualSerializer, tree);
  };
  JsonContentPolymorphicSerializer.prototype.throwSubtypeNotRegistered_wimvgz$_0 = function (subClass, baseClass) {
    var tmp$;
    var subClassName = (tmp$ = subClass.simpleName) != null ? tmp$ : subClass.toString();
    var scope = "in the scope of '" + toString(baseClass.simpleName) + "'";
    throw SerializationException_init("Class '" + subClassName + "' is not registered for polymorphic serialization " + scope + '.' + '\n' + "Mark the base class as 'sealed' or register the serializer explicitly.");
  };
  JsonContentPolymorphicSerializer.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonContentPolymorphicSerializer',
    interfaces: [KSerializer]
  };
  function JsonDecoder() {
  }
  JsonDecoder.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'JsonDecoder',
    interfaces: [CompositeDecoder, Decoder]
  };
  function JsonElement() {
    JsonElement$Companion_getInstance();
  }
  function JsonElement$Companion() {
    JsonElement$Companion_instance = this;
  }
  JsonElement$Companion.prototype.serializer = function () {
    return JsonElementSerializer_getInstance();
  };
  JsonElement$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var JsonElement$Companion_instance = null;
  function JsonElement$Companion_getInstance() {
    if (JsonElement$Companion_instance === null) {
      new JsonElement$Companion();
    }return JsonElement$Companion_instance;
  }
  JsonElement.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonElement',
    interfaces: []
  };
  function JsonPrimitive() {
    JsonPrimitive$Companion_getInstance();
    JsonElement.call(this);
  }
  JsonPrimitive.prototype.toString = function () {
    return this.content;
  };
  function JsonPrimitive$Companion() {
    JsonPrimitive$Companion_instance = this;
  }
  JsonPrimitive$Companion.prototype.serializer = function () {
    return JsonPrimitiveSerializer_getInstance();
  };
  JsonPrimitive$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var JsonPrimitive$Companion_instance = null;
  function JsonPrimitive$Companion_getInstance() {
    if (JsonPrimitive$Companion_instance === null) {
      new JsonPrimitive$Companion();
    }return JsonPrimitive$Companion_instance;
  }
  JsonPrimitive.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonPrimitive',
    interfaces: [JsonElement]
  };
  function JsonPrimitive_0(value) {
    if (value == null)
      return JsonNull_getInstance();
    return new JsonLiteral(value, false);
  }
  function JsonPrimitive_1(value) {
    if (value == null)
      return JsonNull_getInstance();
    return new JsonLiteral(value, false);
  }
  function JsonPrimitive_2(value) {
    if (value == null)
      return JsonNull_getInstance();
    return new JsonLiteral(value, true);
  }
  function JsonLiteral(body, isString) {
    JsonPrimitive.call(this);
    this.isString_jhh1on$_0 = isString;
    this.content_prrjtz$_0 = body.toString();
  }
  Object.defineProperty(JsonLiteral.prototype, 'isString', {
    get: function () {
      return this.isString_jhh1on$_0;
    }
  });
  Object.defineProperty(JsonLiteral.prototype, 'content', {
    configurable: true,
    get: function () {
      return this.content_prrjtz$_0;
    }
  });
  JsonLiteral.prototype.toString = function () {
    var tmp$;
    if (this.isString) {
      var $receiver = StringBuilder_init();
      printQuoted($receiver, this.content);
      tmp$ = $receiver.toString();
    } else
      tmp$ = this.content;
    return tmp$;
  };
  JsonLiteral.prototype.equals = function (other) {
    var tmp$, tmp$_0;
    if (this === other)
      return true;
    if (other == null || !((tmp$ = Kotlin.getKClassFromExpression(this)) != null ? tmp$.equals(Kotlin.getKClassFromExpression(other)) : null))
      return false;
    Kotlin.isType(tmp$_0 = other, JsonLiteral) ? tmp$_0 : throwCCE();
    if (this.isString !== other.isString)
      return false;
    if (!equals(this.content, other.content))
      return false;
    return true;
  };
  JsonLiteral.prototype.hashCode = function () {
    var result = hashCode(this.isString);
    result = (31 * result | 0) + hashCode(this.content) | 0;
    return result;
  };
  JsonLiteral.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonLiteral',
    interfaces: [JsonPrimitive]
  };
  function JsonNull() {
    JsonNull_instance = this;
    JsonPrimitive.call(this);
    this.content_w1vkof$_0 = 'null';
  }
  Object.defineProperty(JsonNull.prototype, 'isString', {
    configurable: true,
    get: function () {
      return false;
    }
  });
  Object.defineProperty(JsonNull.prototype, 'content', {
    configurable: true,
    get: function () {
      return this.content_w1vkof$_0;
    }
  });
  JsonNull.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonNull',
    interfaces: [JsonPrimitive]
  };
  var JsonNull_instance = null;
  function JsonNull_getInstance() {
    if (JsonNull_instance === null) {
      new JsonNull();
    }return JsonNull_instance;
  }
  function JsonObject(content) {
    JsonObject$Companion_getInstance();
    JsonElement.call(this);
    this.content_0 = content;
  }
  JsonObject.prototype.equals = function (other) {
    return equals(this.content_0, other);
  };
  JsonObject.prototype.hashCode = function () {
    return hashCode(this.content_0);
  };
  function JsonObject$toString$lambda(f) {
    var k = f.key;
    var v = f.value;
    var $receiver = StringBuilder_init();
    printQuoted($receiver, k);
    $receiver.append_s8itvh$(58);
    $receiver.append_s8jyv4$(v);
    return $receiver.toString();
  }
  JsonObject.prototype.toString = function () {
    return joinToString(this.content_0.entries, ',', '{', '}', void 0, void 0, JsonObject$toString$lambda);
  };
  function JsonObject$Companion() {
    JsonObject$Companion_instance = this;
  }
  JsonObject$Companion.prototype.serializer = function () {
    return JsonObjectSerializer_getInstance();
  };
  JsonObject$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var JsonObject$Companion_instance = null;
  function JsonObject$Companion_getInstance() {
    if (JsonObject$Companion_instance === null) {
      new JsonObject$Companion();
    }return JsonObject$Companion_instance;
  }
  Object.defineProperty(JsonObject.prototype, 'entries', {
    configurable: true,
    get: function () {
      return this.content_0.entries;
    }
  });
  Object.defineProperty(JsonObject.prototype, 'keys', {
    configurable: true,
    get: function () {
      return this.content_0.keys;
    }
  });
  Object.defineProperty(JsonObject.prototype, 'size', {
    configurable: true,
    get: function () {
      return this.content_0.size;
    }
  });
  Object.defineProperty(JsonObject.prototype, 'values', {
    configurable: true,
    get: function () {
      return this.content_0.values;
    }
  });
  JsonObject.prototype.containsKey_11rb$ = function (key) {
    return this.content_0.containsKey_11rb$(key);
  };
  JsonObject.prototype.containsValue_11rc$ = function (value) {
    return this.content_0.containsValue_11rc$(value);
  };
  JsonObject.prototype.get_11rb$ = function (key) {
    return this.content_0.get_11rb$(key);
  };
  JsonObject.prototype.isEmpty = function () {
    return this.content_0.isEmpty();
  };
  JsonObject.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonObject',
    interfaces: [Map, JsonElement]
  };
  function JsonArray(content) {
    JsonArray$Companion_getInstance();
    JsonElement.call(this);
    this.content_0 = content;
  }
  JsonArray.prototype.equals = function (other) {
    return equals(this.content_0, other);
  };
  JsonArray.prototype.hashCode = function () {
    return hashCode(this.content_0);
  };
  JsonArray.prototype.toString = function () {
    return joinToString(this.content_0, ',', '[', ']');
  };
  function JsonArray$Companion() {
    JsonArray$Companion_instance = this;
  }
  JsonArray$Companion.prototype.serializer = function () {
    return JsonArraySerializer_getInstance();
  };
  JsonArray$Companion.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Companion',
    interfaces: []
  };
  var JsonArray$Companion_instance = null;
  function JsonArray$Companion_getInstance() {
    if (JsonArray$Companion_instance === null) {
      new JsonArray$Companion();
    }return JsonArray$Companion_instance;
  }
  Object.defineProperty(JsonArray.prototype, 'size', {
    configurable: true,
    get: function () {
      return this.content_0.size;
    }
  });
  JsonArray.prototype.contains_11rb$ = function (element) {
    return this.content_0.contains_11rb$(element);
  };
  JsonArray.prototype.containsAll_brywnq$ = function (elements) {
    return this.content_0.containsAll_brywnq$(elements);
  };
  JsonArray.prototype.get_za3lpa$ = function (index) {
    return this.content_0.get_za3lpa$(index);
  };
  JsonArray.prototype.indexOf_11rb$ = function (element) {
    return this.content_0.indexOf_11rb$(element);
  };
  JsonArray.prototype.isEmpty = function () {
    return this.content_0.isEmpty();
  };
  JsonArray.prototype.iterator = function () {
    return this.content_0.iterator();
  };
  JsonArray.prototype.lastIndexOf_11rb$ = function (element) {
    return this.content_0.lastIndexOf_11rb$(element);
  };
  JsonArray.prototype.listIterator = function () {
    return this.content_0.listIterator();
  };
  JsonArray.prototype.listIterator_za3lpa$ = function (index) {
    return this.content_0.listIterator_za3lpa$(index);
  };
  JsonArray.prototype.subList_vux9f0$ = function (fromIndex, toIndex) {
    return this.content_0.subList_vux9f0$(fromIndex, toIndex);
  };
  JsonArray.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonArray',
    interfaces: [List, JsonElement]
  };
  function get_jsonPrimitive($receiver) {
    var tmp$, tmp$_0;
    return (tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonPrimitive) ? tmp$ : null) != null ? tmp$_0 : error($receiver, 'JsonPrimitive');
  }
  function get_jsonObject($receiver) {
    var tmp$, tmp$_0;
    return (tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonObject) ? tmp$ : null) != null ? tmp$_0 : error($receiver, 'JsonObject');
  }
  function get_jsonArray($receiver) {
    var tmp$, tmp$_0;
    return (tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonArray) ? tmp$ : null) != null ? tmp$_0 : error($receiver, 'JsonArray');
  }
  function get_jsonNull($receiver) {
    var tmp$, tmp$_0;
    return (tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonNull) ? tmp$ : null) != null ? tmp$_0 : error($receiver, 'JsonNull');
  }
  function get_int($receiver) {
    return toInt($receiver.content);
  }
  function get_intOrNull($receiver) {
    return toIntOrNull($receiver.content);
  }
  function get_long($receiver) {
    return toLong($receiver.content);
  }
  function get_longOrNull($receiver) {
    return toLongOrNull($receiver.content);
  }
  function get_double($receiver) {
    return toDouble($receiver.content);
  }
  function get_doubleOrNull($receiver) {
    return toDoubleOrNull($receiver.content);
  }
  function get_float($receiver) {
    return toDouble($receiver.content);
  }
  function get_floatOrNull($receiver) {
    return toDoubleOrNull($receiver.content);
  }
  function get_boolean($receiver) {
    var tmp$;
    tmp$ = toBooleanStrictOrNull($receiver.content);
    if (tmp$ == null) {
      throw IllegalStateException_init($receiver.toString() + ' does not represent a Boolean');
    }return tmp$;
  }
  function get_booleanOrNull($receiver) {
    return toBooleanStrictOrNull($receiver.content);
  }
  function get_contentOrNull($receiver) {
    return Kotlin.isType($receiver, JsonNull) ? null : $receiver.content;
  }
  function error($receiver, element) {
    throw IllegalArgumentException_init('Element ' + Kotlin.getKClassFromExpression($receiver) + ' is not a ' + element);
  }
  function unexpectedJson(key, expected) {
    throw IllegalArgumentException_init('Element ' + key + ' is not a ' + expected);
  }
  var buildJsonObject = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.buildJsonObject_s5o6vg$', wrapFunction(function () {
    var JsonObjectBuilder_init = _.kotlinx.serialization.json.JsonObjectBuilder;
    return function (builderAction) {
      var builder = new JsonObjectBuilder_init();
      builderAction(builder);
      return builder.build();
    };
  }));
  var buildJsonArray = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.buildJsonArray_mb52fq$', wrapFunction(function () {
    var JsonArrayBuilder_init = _.kotlinx.serialization.json.JsonArrayBuilder;
    return function (builderAction) {
      var builder = new JsonArrayBuilder_init();
      builderAction(builder);
      return builder.build();
    };
  }));
  function JsonObjectBuilder() {
    this.content_0 = LinkedHashMap_init();
  }
  JsonObjectBuilder.prototype.put_zafu29$ = function (key, element) {
    return this.content_0.put_xwzc9p$(key, element);
  };
  JsonObjectBuilder.prototype.build = function () {
    return new JsonObject(this.content_0);
  };
  JsonObjectBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonObjectBuilder',
    interfaces: []
  };
  function putJsonObject($receiver, key, builderAction) {
    var builder = new JsonObjectBuilder();
    builderAction(builder);
    return $receiver.put_zafu29$(key, builder.build());
  }
  function putJsonArray($receiver, key, builderAction) {
    var builder = new JsonArrayBuilder();
    builderAction(builder);
    return $receiver.put_zafu29$(key, builder.build());
  }
  function put($receiver, key, value) {
    return $receiver.put_zafu29$(key, JsonPrimitive_0(value));
  }
  function put_0($receiver, key, value) {
    return $receiver.put_zafu29$(key, JsonPrimitive_1(value));
  }
  function put_1($receiver, key, value) {
    return $receiver.put_zafu29$(key, JsonPrimitive_2(value));
  }
  function JsonArrayBuilder() {
    this.content_0 = ArrayList_init();
  }
  JsonArrayBuilder.prototype.add_qiw0cd$ = function (element) {
    this.content_0.add_11rb$(element);
    return true;
  };
  JsonArrayBuilder.prototype.build = function () {
    return new JsonArray(this.content_0);
  };
  JsonArrayBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonArrayBuilder',
    interfaces: []
  };
  function add($receiver, value) {
    return $receiver.add_qiw0cd$(JsonPrimitive_0(value));
  }
  function add_0($receiver, value) {
    return $receiver.add_qiw0cd$(JsonPrimitive_1(value));
  }
  function add_1($receiver, value) {
    return $receiver.add_qiw0cd$(JsonPrimitive_2(value));
  }
  function addJsonObject($receiver, builderAction) {
    var builder = new JsonObjectBuilder();
    builderAction(builder);
    return $receiver.add_qiw0cd$(builder.build());
  }
  function addJsonArray($receiver, builderAction) {
    var builder = new JsonArrayBuilder();
    builderAction(builder);
    return $receiver.add_qiw0cd$(builder.build());
  }
  var infixToDeprecated;
  var unaryPlusDeprecated;
  function JsonDslMarker() {
  }
  JsonDslMarker.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonDslMarker',
    interfaces: [Annotation]
  };
  function JsonElementSerializer() {
    JsonElementSerializer_instance = this;
    this.descriptor_u8kpse$_0 = buildSerialDescriptor('kotlinx.serialization.json.JsonElement', PolymorphicKind.SEALED, [], JsonElementSerializer$descriptor$lambda);
  }
  Object.defineProperty(JsonElementSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_u8kpse$_0;
    }
  });
  JsonElementSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    verify(encoder);
    if (Kotlin.isType(value, JsonPrimitive))
      encoder.encodeSerializableValue_tf03ej$(JsonPrimitiveSerializer_getInstance(), value);
    else if (Kotlin.isType(value, JsonObject))
      encoder.encodeSerializableValue_tf03ej$(JsonObjectSerializer_getInstance(), value);
    else if (Kotlin.isType(value, JsonArray))
      encoder.encodeSerializableValue_tf03ej$(JsonArraySerializer_getInstance(), value);
  };
  JsonElementSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    var input = asJsonDecoder(decoder);
    return input.decodeJsonElement();
  };
  function JsonElementSerializer$descriptor$lambda$lambda() {
    return JsonPrimitiveSerializer_getInstance().descriptor;
  }
  function JsonElementSerializer$descriptor$lambda$lambda_0() {
    return JsonNullSerializer_getInstance().descriptor;
  }
  function JsonElementSerializer$descriptor$lambda$lambda_1() {
    return JsonLiteralSerializer_getInstance().descriptor;
  }
  function JsonElementSerializer$descriptor$lambda$lambda_2() {
    return JsonObjectSerializer_getInstance().descriptor;
  }
  function JsonElementSerializer$descriptor$lambda$lambda_3() {
    return JsonArraySerializer_getInstance().descriptor;
  }
  function JsonElementSerializer$descriptor$lambda($receiver) {
    $receiver.element_vxrguq$('JsonPrimitive', defer(JsonElementSerializer$descriptor$lambda$lambda));
    $receiver.element_vxrguq$('JsonNull', defer(JsonElementSerializer$descriptor$lambda$lambda_0));
    $receiver.element_vxrguq$('JsonLiteral', defer(JsonElementSerializer$descriptor$lambda$lambda_1));
    $receiver.element_vxrguq$('JsonObject', defer(JsonElementSerializer$descriptor$lambda$lambda_2));
    $receiver.element_vxrguq$('JsonArray', defer(JsonElementSerializer$descriptor$lambda$lambda_3));
    return Unit;
  }
  JsonElementSerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonElementSerializer',
    interfaces: [KSerializer]
  };
  var JsonElementSerializer_instance = null;
  function JsonElementSerializer_getInstance() {
    if (JsonElementSerializer_instance === null) {
      new JsonElementSerializer();
    }return JsonElementSerializer_instance;
  }
  function JsonPrimitiveSerializer() {
    JsonPrimitiveSerializer_instance = this;
    this.descriptor_1d7xi5$_0 = buildSerialDescriptor('kotlinx.serialization.json.JsonPrimitive', PrimitiveKind.STRING, []);
  }
  Object.defineProperty(JsonPrimitiveSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_1d7xi5$_0;
    }
  });
  JsonPrimitiveSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    var tmp$, tmp$_0;
    verify(encoder);
    if (Kotlin.isType(value, JsonNull)) {
      tmp$_0 = encoder.encodeSerializableValue_tf03ej$(JsonNullSerializer_getInstance(), JsonNull_getInstance());
    } else {
      tmp$_0 = encoder.encodeSerializableValue_tf03ej$(JsonLiteralSerializer_getInstance(), Kotlin.isType(tmp$ = value, JsonLiteral) ? tmp$ : throwCCE());
    }
    return tmp$_0;
  };
  JsonPrimitiveSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    var result = asJsonDecoder(decoder).decodeJsonElement();
    if (!Kotlin.isType(result, JsonPrimitive))
      throw JsonDecodingException_1(-1, 'Unexpected JSON element, expected JsonPrimitive, had ' + Kotlin.getKClassFromExpression(result), result.toString());
    return result;
  };
  JsonPrimitiveSerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonPrimitiveSerializer',
    interfaces: [KSerializer]
  };
  var JsonPrimitiveSerializer_instance = null;
  function JsonPrimitiveSerializer_getInstance() {
    if (JsonPrimitiveSerializer_instance === null) {
      new JsonPrimitiveSerializer();
    }return JsonPrimitiveSerializer_instance;
  }
  function JsonNullSerializer() {
    JsonNullSerializer_instance = this;
    this.descriptor_kuqqdr$_0 = buildSerialDescriptor('kotlinx.serialization.json.JsonNull', SerialKind.ENUM, []);
  }
  Object.defineProperty(JsonNullSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_kuqqdr$_0;
    }
  });
  JsonNullSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    verify(encoder);
    encoder.encodeNull();
  };
  JsonNullSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    verify_0(decoder);
    if (decoder.decodeNotNullMark()) {
      throw new JsonDecodingException("Expected 'null' literal");
    }decoder.decodeNull();
    return JsonNull_getInstance();
  };
  JsonNullSerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonNullSerializer',
    interfaces: [KSerializer]
  };
  var JsonNullSerializer_instance = null;
  function JsonNullSerializer_getInstance() {
    if (JsonNullSerializer_instance === null) {
      new JsonNullSerializer();
    }return JsonNullSerializer_instance;
  }
  function JsonLiteralSerializer() {
    JsonLiteralSerializer_instance = this;
    this.descriptor_fnzu3f$_0 = PrimitiveSerialDescriptor('kotlinx.serialization.json.JsonLiteral', PrimitiveKind.STRING);
  }
  Object.defineProperty(JsonLiteralSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_fnzu3f$_0;
    }
  });
  JsonLiteralSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    verify(encoder);
    if (value.isString) {
      return encoder.encodeString_61zpoe$(value.content);
    }if ((tmp$ = get_longOrNull(value)) != null) {
      return encoder.encodeLong_s8cxhz$(tmp$);
    }if ((tmp$_0 = toULongOrNull(value.content)) != null) {
      var tmp$_3;
      if ((tmp$_3 = encoder.encodeInline_24f42q$(serializer_0(ULong_init.Companion).descriptor)) != null) {
        tmp$_3.encodeLong_s8cxhz$(tmp$_0.data);
      }return;
    }if ((tmp$_1 = get_doubleOrNull(value)) != null) {
      return encoder.encodeDouble_14dthe$(tmp$_1);
    }if ((tmp$_2 = get_booleanOrNull(value)) != null) {
      return encoder.encodeBoolean_6taknv$(tmp$_2);
    }encoder.encodeString_61zpoe$(value.content);
  };
  JsonLiteralSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    var result = asJsonDecoder(decoder).decodeJsonElement();
    if (!Kotlin.isType(result, JsonLiteral))
      throw JsonDecodingException_1(-1, 'Unexpected JSON element, expected JsonLiteral, had ' + Kotlin.getKClassFromExpression(result), result.toString());
    return result;
  };
  JsonLiteralSerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonLiteralSerializer',
    interfaces: [KSerializer]
  };
  var JsonLiteralSerializer_instance = null;
  function JsonLiteralSerializer_getInstance() {
    if (JsonLiteralSerializer_instance === null) {
      new JsonLiteralSerializer();
    }return JsonLiteralSerializer_instance;
  }
  function JsonObjectSerializer() {
    JsonObjectSerializer_instance = this;
    this.descriptor_a992tj$_0 = JsonObjectSerializer$JsonObjectDescriptor_getInstance();
  }
  function JsonObjectSerializer$JsonObjectDescriptor() {
    JsonObjectSerializer$JsonObjectDescriptor_instance = this;
    var tmp$;
    this.$delegate_p32uvu$_0 = (Kotlin.isType(tmp$ = serializer_4(createKType(getKClass(HashMap), [createInvariantKTypeProjection(createKType(PrimitiveClasses$stringClass, [], false)), createInvariantKTypeProjection(createKType(getKClass(JsonElement), [], false))], false)), KSerializer) ? tmp$ : throwCCE()).descriptor;
    this.serialName_yjsqqv$_0 = 'kotlinx.serialization.json.JsonObject';
  }
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'serialName', {
    configurable: true,
    get: function () {
      return this.serialName_yjsqqv$_0;
    }
  });
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'annotations', {
    configurable: true,
    get: function () {
      return this.$delegate_p32uvu$_0.annotations;
    }
  });
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'elementsCount', {
    configurable: true,
    get: function () {
      return this.$delegate_p32uvu$_0.elementsCount;
    }
  });
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'isInline', {
    configurable: true,
    get: function () {
      return this.$delegate_p32uvu$_0.isInline;
    }
  });
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'isNullable', {
    configurable: true,
    get: function () {
      return this.$delegate_p32uvu$_0.isNullable;
    }
  });
  Object.defineProperty(JsonObjectSerializer$JsonObjectDescriptor.prototype, 'kind', {
    configurable: true,
    get: function () {
      return this.$delegate_p32uvu$_0.kind;
    }
  });
  JsonObjectSerializer$JsonObjectDescriptor.prototype.getElementAnnotations_za3lpa$ = function (index) {
    return this.$delegate_p32uvu$_0.getElementAnnotations_za3lpa$(index);
  };
  JsonObjectSerializer$JsonObjectDescriptor.prototype.getElementDescriptor_za3lpa$ = function (index) {
    return this.$delegate_p32uvu$_0.getElementDescriptor_za3lpa$(index);
  };
  JsonObjectSerializer$JsonObjectDescriptor.prototype.getElementIndex_61zpoe$ = function (name) {
    return this.$delegate_p32uvu$_0.getElementIndex_61zpoe$(name);
  };
  JsonObjectSerializer$JsonObjectDescriptor.prototype.getElementName_za3lpa$ = function (index) {
    return this.$delegate_p32uvu$_0.getElementName_za3lpa$(index);
  };
  JsonObjectSerializer$JsonObjectDescriptor.prototype.isElementOptional_za3lpa$ = function (index) {
    return this.$delegate_p32uvu$_0.isElementOptional_za3lpa$(index);
  };
  JsonObjectSerializer$JsonObjectDescriptor.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonObjectDescriptor',
    interfaces: [SerialDescriptor]
  };
  var JsonObjectSerializer$JsonObjectDescriptor_instance = null;
  function JsonObjectSerializer$JsonObjectDescriptor_getInstance() {
    if (JsonObjectSerializer$JsonObjectDescriptor_instance === null) {
      new JsonObjectSerializer$JsonObjectDescriptor();
    }return JsonObjectSerializer$JsonObjectDescriptor_instance;
  }
  Object.defineProperty(JsonObjectSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_a992tj$_0;
    }
  });
  JsonObjectSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    verify(encoder);
    MapSerializer(serializer_3(kotlin_js_internal_StringCompanionObject), JsonElementSerializer_getInstance()).serialize_55azsf$(encoder, value);
  };
  JsonObjectSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    verify_0(decoder);
    return new JsonObject(MapSerializer(serializer_3(kotlin_js_internal_StringCompanionObject), JsonElementSerializer_getInstance()).deserialize_bq71mq$(decoder));
  };
  JsonObjectSerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonObjectSerializer',
    interfaces: [KSerializer]
  };
  var JsonObjectSerializer_instance = null;
  function JsonObjectSerializer_getInstance() {
    if (JsonObjectSerializer_instance === null) {
      new JsonObjectSerializer();
    }return JsonObjectSerializer_instance;
  }
  function JsonArraySerializer() {
    JsonArraySerializer_instance = this;
    this.descriptor_935ivj$_0 = JsonArraySerializer$JsonArrayDescriptor_getInstance();
  }
  function JsonArraySerializer$JsonArrayDescriptor() {
    JsonArraySerializer$JsonArrayDescriptor_instance = this;
    var tmp$;
    this.$delegate_az4ckk$_0 = (Kotlin.isType(tmp$ = serializer_4(createKType(getKClass(List), [createInvariantKTypeProjection(createKType(getKClass(JsonElement), [], false))], false)), KSerializer) ? tmp$ : throwCCE()).descriptor;
    this.serialName_bqe1pt$_0 = 'kotlinx.serialization.json.JsonArray';
  }
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'serialName', {
    configurable: true,
    get: function () {
      return this.serialName_bqe1pt$_0;
    }
  });
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'annotations', {
    configurable: true,
    get: function () {
      return this.$delegate_az4ckk$_0.annotations;
    }
  });
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'elementsCount', {
    configurable: true,
    get: function () {
      return this.$delegate_az4ckk$_0.elementsCount;
    }
  });
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'isInline', {
    configurable: true,
    get: function () {
      return this.$delegate_az4ckk$_0.isInline;
    }
  });
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'isNullable', {
    configurable: true,
    get: function () {
      return this.$delegate_az4ckk$_0.isNullable;
    }
  });
  Object.defineProperty(JsonArraySerializer$JsonArrayDescriptor.prototype, 'kind', {
    configurable: true,
    get: function () {
      return this.$delegate_az4ckk$_0.kind;
    }
  });
  JsonArraySerializer$JsonArrayDescriptor.prototype.getElementAnnotations_za3lpa$ = function (index) {
    return this.$delegate_az4ckk$_0.getElementAnnotations_za3lpa$(index);
  };
  JsonArraySerializer$JsonArrayDescriptor.prototype.getElementDescriptor_za3lpa$ = function (index) {
    return this.$delegate_az4ckk$_0.getElementDescriptor_za3lpa$(index);
  };
  JsonArraySerializer$JsonArrayDescriptor.prototype.getElementIndex_61zpoe$ = function (name) {
    return this.$delegate_az4ckk$_0.getElementIndex_61zpoe$(name);
  };
  JsonArraySerializer$JsonArrayDescriptor.prototype.getElementName_za3lpa$ = function (index) {
    return this.$delegate_az4ckk$_0.getElementName_za3lpa$(index);
  };
  JsonArraySerializer$JsonArrayDescriptor.prototype.isElementOptional_za3lpa$ = function (index) {
    return this.$delegate_az4ckk$_0.isElementOptional_za3lpa$(index);
  };
  JsonArraySerializer$JsonArrayDescriptor.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonArrayDescriptor',
    interfaces: [SerialDescriptor]
  };
  var JsonArraySerializer$JsonArrayDescriptor_instance = null;
  function JsonArraySerializer$JsonArrayDescriptor_getInstance() {
    if (JsonArraySerializer$JsonArrayDescriptor_instance === null) {
      new JsonArraySerializer$JsonArrayDescriptor();
    }return JsonArraySerializer$JsonArrayDescriptor_instance;
  }
  Object.defineProperty(JsonArraySerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.descriptor_935ivj$_0;
    }
  });
  JsonArraySerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    verify(encoder);
    ListSerializer(JsonElementSerializer_getInstance()).serialize_55azsf$(encoder, value);
  };
  JsonArraySerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    verify_0(decoder);
    return new JsonArray(ListSerializer(JsonElementSerializer_getInstance()).deserialize_bq71mq$(decoder));
  };
  JsonArraySerializer.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'JsonArraySerializer',
    interfaces: [KSerializer]
  };
  var JsonArraySerializer_instance = null;
  function JsonArraySerializer_getInstance() {
    if (JsonArraySerializer_instance === null) {
      new JsonArraySerializer();
    }return JsonArraySerializer_instance;
  }
  function verify(encoder) {
    asJsonEncoder(encoder);
  }
  function verify_0(decoder) {
    asJsonDecoder(decoder);
  }
  function asJsonDecoder($receiver) {
    var tmp$, tmp$_0;
    tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonDecoder) ? tmp$ : null;
    if (tmp$_0 == null) {
      throw IllegalStateException_init('This serializer can be used only with Json format.' + ('Expected Decoder to be JsonDecoder, got ' + Kotlin.getKClassFromExpression($receiver)));
    }return tmp$_0;
  }
  function asJsonEncoder($receiver) {
    var tmp$, tmp$_0;
    tmp$_0 = Kotlin.isType(tmp$ = $receiver, JsonEncoder) ? tmp$ : null;
    if (tmp$_0 == null) {
      throw IllegalStateException_init('This serializer can be used only with Json format.' + ('Expected Encoder to be JsonEncoder, got ' + Kotlin.getKClassFromExpression($receiver)));
    }return tmp$_0;
  }
  function defer$ObjectLiteral(closure$deferred) {
    this.original_yfx7kf$_0 = lazy(closure$deferred);
  }
  Object.defineProperty(defer$ObjectLiteral.prototype, 'original_0', {
    configurable: true,
    get: function () {
      return this.original_yfx7kf$_0.value;
    }
  });
  Object.defineProperty(defer$ObjectLiteral.prototype, 'serialName', {
    configurable: true,
    get: function () {
      return this.original_0.serialName;
    }
  });
  Object.defineProperty(defer$ObjectLiteral.prototype, 'kind', {
    configurable: true,
    get: function () {
      return this.original_0.kind;
    }
  });
  Object.defineProperty(defer$ObjectLiteral.prototype, 'elementsCount', {
    configurable: true,
    get: function () {
      return this.original_0.elementsCount;
    }
  });
  defer$ObjectLiteral.prototype.getElementName_za3lpa$ = function (index) {
    return this.original_0.getElementName_za3lpa$(index);
  };
  defer$ObjectLiteral.prototype.getElementIndex_61zpoe$ = function (name) {
    return this.original_0.getElementIndex_61zpoe$(name);
  };
  defer$ObjectLiteral.prototype.getElementAnnotations_za3lpa$ = function (index) {
    return this.original_0.getElementAnnotations_za3lpa$(index);
  };
  defer$ObjectLiteral.prototype.getElementDescriptor_za3lpa$ = function (index) {
    return this.original_0.getElementDescriptor_za3lpa$(index);
  };
  defer$ObjectLiteral.prototype.isElementOptional_za3lpa$ = function (index) {
    return this.original_0.isElementOptional_za3lpa$(index);
  };
  defer$ObjectLiteral.$metadata$ = {
    kind: Kind_CLASS,
    interfaces: [SerialDescriptor]
  };
  function defer(deferred) {
    return new defer$ObjectLiteral(deferred);
  }
  function JsonEncoder() {
  }
  JsonEncoder.$metadata$ = {
    kind: Kind_INTERFACE,
    simpleName: 'JsonEncoder',
    interfaces: [CompositeEncoder, Encoder]
  };
  function JsonNames(names) {
    this.names = names;
  }
  JsonNames.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonNames',
    interfaces: [Annotation]
  };
  function JsonTransformingSerializer(tSerializer) {
    this.tSerializer_nuzucf$_0 = tSerializer;
  }
  Object.defineProperty(JsonTransformingSerializer.prototype, 'descriptor', {
    configurable: true,
    get: function () {
      return this.tSerializer_nuzucf$_0.descriptor;
    }
  });
  JsonTransformingSerializer.prototype.serialize_55azsf$ = function (encoder, value) {
    var output = asJsonEncoder(encoder);
    var element = writeJson(output.json, value, this.tSerializer_nuzucf$_0);
    element = this.transformSerialize_qiw0cd$(element);
    output.encodeJsonElement_qiw0cd$(element);
  };
  JsonTransformingSerializer.prototype.deserialize_bq71mq$ = function (decoder) {
    var input = asJsonDecoder(decoder);
    var element = input.decodeJsonElement();
    return input.json.decodeFromJsonElement_htt2tq$(this.tSerializer_nuzucf$_0, this.transformDeserialize_qiw0cd$(element));
  };
  JsonTransformingSerializer.prototype.transformDeserialize_qiw0cd$ = function (element) {
    return element;
  };
  JsonTransformingSerializer.prototype.transformSerialize_qiw0cd$ = function (element) {
    return element;
  };
  JsonTransformingSerializer.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonTransformingSerializer',
    interfaces: [KSerializer]
  };
  function decodeFromDynamic($receiver, deserializer, dynamic) {
    return decodeDynamic($receiver, deserializer, dynamic);
  }
  var decodeFromDynamic_0 = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.decodeFromDynamic_is5yld$', wrapFunction(function () {
    var getReifiedTypeParameterKType = Kotlin.getReifiedTypeParameterKType;
    var decodeFromDynamic = _.kotlinx.serialization.json.decodeFromDynamic_76vv94$;
    var serializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.serializer_ca95z9$;
    var KSerializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.KSerializer;
    var throwCCE = Kotlin.throwCCE;
    return function (T_0, isT, $receiver, dynamic) {
      var $receiver_0 = $receiver.serializersModule;
      var tmp$;
      return decodeFromDynamic($receiver, Kotlin.isType(tmp$ = serializer($receiver_0, getReifiedTypeParameterKType(T_0)), KSerializer) ? tmp$ : throwCCE(), dynamic);
    };
  }));
  function encodeToDynamic($receiver, serializer, value) {
    return encodeDynamic($receiver, serializer, value);
  }
  var encodeToDynamic_0 = defineInlineFunction('kotlinx-serialization-kotlinx-serialization-json-js-legacy.kotlinx.serialization.json.encodeToDynamic_eahrye$', wrapFunction(function () {
    var getReifiedTypeParameterKType = Kotlin.getReifiedTypeParameterKType;
    var encodeToDynamic = _.kotlinx.serialization.json.encodeToDynamic_9ibeu3$;
    var serializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.serializer_ca95z9$;
    var KSerializer = _.$$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'].kotlinx.serialization.KSerializer;
    var throwCCE = Kotlin.throwCCE;
    return function (T_0, isT, $receiver, value) {
      var $receiver_0 = $receiver.serializersModule;
      var tmp$;
      return encodeToDynamic($receiver, Kotlin.isType(tmp$ = serializer($receiver_0, getReifiedTypeParameterKType(T_0)), KSerializer) ? tmp$ : throwCCE(), value);
    };
  }));
  function createMapForCache(initialCapacity) {
    return HashMap_init(initialCapacity);
  }
  var MAX_SAFE_INTEGER;
  function decodeDynamic($receiver, deserializer, dynamic) {
    var tmp$;
    switch (typeof dynamic) {
      case 'boolean':
      case 'number':
      case 'string':
        tmp$ = new PrimitiveDynamicInput(dynamic, $receiver);
        break;
      default:if (Array.isArray(dynamic)) {
          tmp$ = new DynamicListInput(dynamic, $receiver);
        } else {
          tmp$ = new DynamicInput(dynamic, $receiver);
        }

        break;
    }
    var input = tmp$;
    return input.decodeSerializableValue_w63s0f$(deserializer);
  }
  function DynamicInput(value, json) {
    NamedValueDecoder.call(this);
    this.value_0 = value;
    this.json_piha9d$_0 = json;
    this.currentPosition_0 = 0;
  }
  Object.defineProperty(DynamicInput.prototype, 'json', {
    get: function () {
      return this.json_piha9d$_0;
    }
  });
  Object.defineProperty(DynamicInput.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.json.serializersModule;
    }
  });
  DynamicInput.prototype.decodeJsonElement = function () {
    var tmp$;
    var tag = this.currentTagOrNull;
    if (tag != null) {
      return decodeFromDynamic(this.json, JsonElement$Companion_getInstance().serializer(), this.value_0[tag]);
    }if (this.value_0 == null) {
      return JsonNull_getInstance();
    }var keys = Object.keys(this.value_0);
    var size = typeof (tmp$ = keys.length) === 'number' ? tmp$ : throwCCE();
    var builder = new JsonObjectBuilder();
    for (var i = 0; i < size; i++) {
      var key = keys[i];
      var value = decodeDynamic(this.json, JsonElement$Companion_getInstance().serializer(), this.value_0[key]);
      builder.put_zafu29$(key.toString(), value);
    }
    return builder.build();
  };
  DynamicInput.prototype.decodeSerializableValue_w63s0f$ = function (deserializer) {
    return decodeSerializableValuePolymorphic(this, deserializer);
  };
  DynamicInput.prototype.composeName_puj7f4$ = function (parentName, childName) {
    return childName;
  };
  DynamicInput.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    var tmp$;
    while (this.currentPosition_0 < descriptor.elementsCount) {
      var name = this.getTag_av9bu7$(descriptor, (tmp$ = this.currentPosition_0, this.currentPosition_0 = tmp$ + 1 | 0, tmp$));
      if (this.value_0[name] !== undefined)
        return this.currentPosition_0 - 1 | 0;
    }
    return -1;
  };
  DynamicInput.prototype.decodeTaggedEnum_gaombr$ = function (tag, enumDescriptor) {
    var tmp$;
    return getJsonNameIndexOrThrow(enumDescriptor, this.json, typeof (tmp$ = this.getByTag_61zpoe$(tag)) === 'string' ? tmp$ : throwCCE());
  };
  DynamicInput.prototype.getByTag_61zpoe$ = function (tag) {
    return this.value_0[tag];
  };
  DynamicInput.prototype.decodeTaggedChar_11rb$ = function (tag) {
    var tmp$;
    var value = this.getByTag_61zpoe$(tag);
    if (typeof value === 'string')
      if (value.length === 1)
        tmp$ = value.charCodeAt(0);
      else
        throw SerializationException_init(value.toString() + " can't be represented as Char");
    else if (Kotlin.isNumber(value))
      tmp$ = numberToChar(value);
    else
      throw SerializationException_init(value.toString() + " can't be represented as Char");
    return toBoxedChar(tmp$);
  };
  DynamicInput.prototype.decodeTaggedLong_11rb$ = function (tag) {
    var tmp$, tmp$_0;
    var value = this.getByTag_61zpoe$(tag);
    tmp$_0 = typeof (tmp$ = value) === 'number' ? tmp$ : null;
    if (tmp$_0 == null) {
      throw SerializationException_init(value.toString() + ' is not a Number');
    }var number = tmp$_0;
    return this.toJavascriptLong_0(number);
  };
  DynamicInput.prototype.toJavascriptLong_0 = function (number) {
    var tmp$ = isFinite_0(number);
    if (tmp$) {
      tmp$ = JsMath.floor(number) === number;
    }var canBeConverted = tmp$;
    if (!canBeConverted)
      throw SerializationException_init(number.toString() + " can't be represented as Long because it is not finite or has non-zero fractional part");
    var inBound = JsMath.abs(number) <= MAX_SAFE_INTEGER;
    if (!inBound)
      throw SerializationException_init(number.toString() + " can't be deserialized to Long due to a potential precision loss");
    return Kotlin.Long.fromNumber(number);
  };
  DynamicInput.prototype.decodeTaggedValue_11rb$ = function (tag) {
    var tmp$, tmp$_0;
    var o = (tmp$ = this.getByTag_61zpoe$(tag)) != null ? tmp$ : this.throwMissingTag_0(tag);
    return Kotlin.isType(tmp$_0 = o, Any) ? tmp$_0 : throwCCE();
  };
  DynamicInput.prototype.decodeTaggedNotNullMark_11rb$ = function (tag) {
    var o = this.getByTag_61zpoe$(tag);
    if (o === undefined)
      this.throwMissingTag_0(tag);
    return o != null;
  };
  DynamicInput.prototype.throwMissingTag_0 = function (tag) {
    throw SerializationException_init('Value for field ' + tag + ' is missing');
  };
  DynamicInput.prototype.beginStructure_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0, tmp$_1, tmp$_2;
    var currentValue = (tmp$_0 = (tmp$ = this.currentTagOrNull) != null ? this.value_0[tmp$] : null) != null ? tmp$_0 : this.value_0;
    if (Kotlin.isType(descriptor.kind, PolymorphicKind))
      tmp$_1 = this.json.configuration.useArrayPolymorphism ? StructureKind.LIST : StructureKind.MAP;
    else
      tmp$_1 = descriptor.kind;
    var kind = tmp$_1;
    if (equals(kind, StructureKind.LIST))
      tmp$_2 = new DynamicListInput(currentValue, this.json);
    else if (equals(kind, StructureKind.MAP))
      tmp$_2 = new DynamicMapInput(currentValue, this.json);
    else
      tmp$_2 = new DynamicInput(currentValue, this.json);
    return tmp$_2;
  };
  DynamicInput.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DynamicInput',
    interfaces: [JsonDecoder, NamedValueDecoder]
  };
  function DynamicMapInput(value, json) {
    DynamicInput.call(this, value, json);
    this.keys_0 = Object.keys(value);
    var tmp$;
    this.size_0 = (typeof (tmp$ = this.keys_0.length) === 'number' ? tmp$ : throwCCE()) * 2 | 0;
    this.currentPosition_1 = -1;
  }
  Object.defineProperty(DynamicMapInput.prototype, 'isKey_0', {
    configurable: true,
    get: function () {
      return this.currentPosition_1 % 2 === 0;
    }
  });
  DynamicMapInput.prototype.elementName_szpzho$ = function (desc, index) {
    var tmp$;
    var i = index / 2 | 0;
    return typeof (tmp$ = this.keys_0[i]) === 'string' ? tmp$ : throwCCE();
  };
  DynamicMapInput.prototype.throwIllegalKeyType_0 = function (tag, value, type) {
    throw SerializationException_init('Property ' + tag + ' is not valid type ' + type + ': ' + value.toString());
  };
  DynamicMapInput.prototype.decodeTaggedByte_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedByte_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }decodeMapKey_0$result = (tmp$ = toByteOrNull(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'byte');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedByte_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeTaggedShort_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedShort_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }decodeMapKey_0$result = (tmp$ = toShortOrNull(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'short');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedShort_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeTaggedInt_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedInt_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }decodeMapKey_0$result = (tmp$ = toIntOrNull(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'int');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedInt_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeTaggedLong_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedLong_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }var $receiver = value.toString();
        var tmp$_0;
        decodeMapKey_0$result = (tmp$ = this.toJavascriptLong_0((tmp$_0 = toDoubleOrNull($receiver)) != null ? tmp$_0 : this.throwIllegalKeyType_0(tag, $receiver, 'long'))) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'long');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedLong_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeTaggedFloat_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedFloat_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }decodeMapKey_0$result = (tmp$ = toDoubleOrNull(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'float');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedFloat_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeTaggedDouble_11rb$ = function (tag) {
    var decodeMapKey_0$result;
    decodeMapKey_0$break: do {
      var tmp$;
      if (this.isKey_0) {
        var value = this.decodeTaggedValue_11rb$(tag);
        if (!(typeof value === 'string')) {
          decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedDouble_11rb$.call(this, tag);
          break decodeMapKey_0$break;
        }decodeMapKey_0$result = (tmp$ = toDoubleOrNull(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, 'double');
        break decodeMapKey_0$break;
      }decodeMapKey_0$result = DynamicInput.prototype.decodeTaggedDouble_11rb$.call(this, tag);
    }
     while (false);
    return decodeMapKey_0$result;
  };
  DynamicMapInput.prototype.decodeMapKey_0 = function (T_0, isT, tag, type, decode, cast) {
    var tmp$;
    if (this.isKey_0) {
      var value = this.decodeTaggedValue_11rb$(tag);
      if (!(typeof value === 'string'))
        return decode(tag);
      return (tmp$ = cast(value.toString())) != null ? tmp$ : this.throwIllegalKeyType_0(tag, value, type);
    }return decode(tag);
  };
  DynamicMapInput.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    var tmp$, tmp$_0;
    while (this.currentPosition_1 < (this.size_0 - 1 | 0)) {
      var i = (tmp$ = this.currentPosition_1, this.currentPosition_1 = tmp$ + 1 | 0, tmp$) / 2 | 0;
      var name = typeof (tmp$_0 = this.keys_0[i]) === 'string' ? tmp$_0 : throwCCE();
      if (this.value_0[name] !== undefined)
        return this.currentPosition_1;
    }
    return -1;
  };
  DynamicMapInput.prototype.getByTag_61zpoe$ = function (tag) {
    return this.currentPosition_1 % 2 === 0 ? tag : this.value_0[tag];
  };
  DynamicMapInput.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DynamicMapInput',
    interfaces: [DynamicInput]
  };
  function DynamicListInput(value, json) {
    DynamicInput.call(this, value, json);
    var tmp$;
    this.size_0 = typeof (tmp$ = value.length) === 'number' ? tmp$ : throwCCE();
    this.currentPosition_1 = -1;
  }
  DynamicListInput.prototype.elementName_szpzho$ = function (desc, index) {
    return index.toString();
  };
  DynamicListInput.prototype.decodeElementIndex_24f42q$ = function (descriptor) {
    while (this.currentPosition_1 < (this.size_0 - 1 | 0)) {
      var o = this.value_0[this.currentPosition_1 = this.currentPosition_1 + 1 | 0, this.currentPosition_1];
      if (o !== undefined)
        return this.currentPosition_1;
    }
    return -1;
  };
  DynamicListInput.prototype.decodeJsonElement = function () {
    var tag = this.currentTagOrNull;
    if (tag != null) {
      return decodeFromDynamic(this.json, JsonElement$Companion_getInstance().serializer(), this.value_0[tag]);
    }var builder = new JsonArrayBuilder();
    var tmp$;
    tmp$ = this.size_0;
    for (var i = 0; i < tmp$; i++) {
      builder.add_qiw0cd$(decodeFromDynamic(this.json, JsonElement$Companion_getInstance().serializer(), this.value_0[i]));
    }
    return builder.build();
  };
  DynamicListInput.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DynamicListInput',
    interfaces: [DynamicInput]
  };
  function PrimitiveDynamicInput(value, json) {
    DynamicInput.call(this, value, json);
    this.pushTag_11rb$('primitive');
  }
  PrimitiveDynamicInput.prototype.getByTag_61zpoe$ = function (tag) {
    return this.value_0;
  };
  PrimitiveDynamicInput.prototype.decodeJsonElement = function () {
    var tmp$;
    var str = this.value_0.toString();
    switch (typeof this.value_0) {
      case 'boolean':
        tmp$ = JsonPrimitive_0(toBoolean(str));
        break;
      case 'number':
        var l = toLongOrNull(str);
        if (l != null)
          return JsonPrimitive_1(l);
        var d = toDoubleOrNull(str);
        if (d != null)
          return JsonPrimitive_1(d);
        return JsonPrimitive_2(str);
      default:tmp$ = JsonPrimitive_2(str);
        break;
    }
    return tmp$;
  };
  PrimitiveDynamicInput.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'PrimitiveDynamicInput',
    interfaces: [DynamicInput]
  };
  function encodeDynamic($receiver, serializer, value) {
    if (Kotlin.isType(serializer.descriptor.kind, PrimitiveKind) || Kotlin.isType(serializer.descriptor.kind, Object.getPrototypeOf(SerialKind.ENUM).constructor)) {
      var encoder = new DynamicPrimitiveEncoder($receiver);
      encoder.encodeSerializableValue_tf03ej$(serializer, value);
      return encoder.result;
    }var encoder_0 = new DynamicObjectEncoder($receiver, false);
    encoder_0.encodeSerializableValue_tf03ej$(serializer, value);
    return encoder_0.result;
  }
  function DynamicObjectEncoder(json, encodeNullAsUndefined) {
    AbstractEncoder.call(this);
    this.json_16lcpu$_0 = json;
    this.encodeNullAsUndefined_0 = encodeNullAsUndefined;
    this.result = DynamicObjectEncoder$NoOutputMark_getInstance();
    this.current_d5mulr$_0 = this.current_d5mulr$_0;
    this.currentName_0 = null;
    this.currentDescriptor_sig5ya$_0 = this.currentDescriptor_sig5ya$_0;
    this.currentElementIsMapKey_0 = false;
    this.writePolymorphic_0 = false;
  }
  Object.defineProperty(DynamicObjectEncoder.prototype, 'json', {
    get: function () {
      return this.json_16lcpu$_0;
    }
  });
  Object.defineProperty(DynamicObjectEncoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.json.serializersModule;
    }
  });
  Object.defineProperty(DynamicObjectEncoder.prototype, 'current_0', {
    configurable: true,
    get: function () {
      if (this.current_d5mulr$_0 == null)
        return throwUPAE('current');
      return this.current_d5mulr$_0;
    },
    set: function (current) {
      this.current_d5mulr$_0 = current;
    }
  });
  Object.defineProperty(DynamicObjectEncoder.prototype, 'currentDescriptor_0', {
    configurable: true,
    get: function () {
      if (this.currentDescriptor_sig5ya$_0 == null)
        return throwUPAE('currentDescriptor');
      return this.currentDescriptor_sig5ya$_0;
    },
    set: function (currentDescriptor) {
      this.currentDescriptor_sig5ya$_0 = currentDescriptor;
    }
  });
  function DynamicObjectEncoder$NoOutputMark() {
    DynamicObjectEncoder$NoOutputMark_instance = this;
  }
  DynamicObjectEncoder$NoOutputMark.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'NoOutputMark',
    interfaces: []
  };
  var DynamicObjectEncoder$NoOutputMark_instance = null;
  function DynamicObjectEncoder$NoOutputMark_getInstance() {
    if (DynamicObjectEncoder$NoOutputMark_instance === null) {
      new DynamicObjectEncoder$NoOutputMark();
    }return DynamicObjectEncoder$NoOutputMark_instance;
  }
  function DynamicObjectEncoder$Node(writeMode, jsObject) {
    this.writeMode = writeMode;
    this.jsObject = jsObject;
    this.index = 0;
    this.parent_95alro$_0 = this.parent_95alro$_0;
  }
  Object.defineProperty(DynamicObjectEncoder$Node.prototype, 'parent', {
    configurable: true,
    get: function () {
      if (this.parent_95alro$_0 == null)
        return throwUPAE('parent');
      return this.parent_95alro$_0;
    },
    set: function (parent) {
      this.parent_95alro$_0 = parent;
    }
  });
  DynamicObjectEncoder$Node.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'Node',
    interfaces: []
  };
  function DynamicObjectEncoder$WriteMode(name, ordinal) {
    Enum.call(this);
    this.name$ = name;
    this.ordinal$ = ordinal;
  }
  function DynamicObjectEncoder$WriteMode_initFields() {
    DynamicObjectEncoder$WriteMode_initFields = function () {
    };
    DynamicObjectEncoder$WriteMode$OBJ_instance = new DynamicObjectEncoder$WriteMode('OBJ', 0);
    DynamicObjectEncoder$WriteMode$MAP_instance = new DynamicObjectEncoder$WriteMode('MAP', 1);
    DynamicObjectEncoder$WriteMode$LIST_instance = new DynamicObjectEncoder$WriteMode('LIST', 2);
  }
  var DynamicObjectEncoder$WriteMode$OBJ_instance;
  function DynamicObjectEncoder$WriteMode$OBJ_getInstance() {
    DynamicObjectEncoder$WriteMode_initFields();
    return DynamicObjectEncoder$WriteMode$OBJ_instance;
  }
  var DynamicObjectEncoder$WriteMode$MAP_instance;
  function DynamicObjectEncoder$WriteMode$MAP_getInstance() {
    DynamicObjectEncoder$WriteMode_initFields();
    return DynamicObjectEncoder$WriteMode$MAP_instance;
  }
  var DynamicObjectEncoder$WriteMode$LIST_instance;
  function DynamicObjectEncoder$WriteMode$LIST_getInstance() {
    DynamicObjectEncoder$WriteMode_initFields();
    return DynamicObjectEncoder$WriteMode$LIST_instance;
  }
  DynamicObjectEncoder$WriteMode.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'WriteMode',
    interfaces: [Enum]
  };
  function DynamicObjectEncoder$WriteMode$values() {
    return [DynamicObjectEncoder$WriteMode$OBJ_getInstance(), DynamicObjectEncoder$WriteMode$MAP_getInstance(), DynamicObjectEncoder$WriteMode$LIST_getInstance()];
  }
  DynamicObjectEncoder$WriteMode.values = DynamicObjectEncoder$WriteMode$values;
  function DynamicObjectEncoder$WriteMode$valueOf(name) {
    switch (name) {
      case 'OBJ':
        return DynamicObjectEncoder$WriteMode$OBJ_getInstance();
      case 'MAP':
        return DynamicObjectEncoder$WriteMode$MAP_getInstance();
      case 'LIST':
        return DynamicObjectEncoder$WriteMode$LIST_getInstance();
      default:throwISE('No enum constant kotlinx.serialization.json.internal.DynamicObjectEncoder.WriteMode.' + name);
    }
  }
  DynamicObjectEncoder$WriteMode.valueOf_61zpoe$ = DynamicObjectEncoder$WriteMode$valueOf;
  DynamicObjectEncoder.prototype.encodeElement_szpzho$ = function (descriptor, index) {
    this.current_0.index = index;
    this.currentDescriptor_0 = descriptor;
    if (this.current_0.writeMode === DynamicObjectEncoder$WriteMode$MAP_getInstance())
      this.currentElementIsMapKey_0 = this.current_0.index % 2 === 0;
    else if (this.current_0.writeMode === DynamicObjectEncoder$WriteMode$LIST_getInstance() && Kotlin.isType(descriptor.kind, PolymorphicKind))
      this.currentName_0 = index.toString();
    else
      this.currentName_0 = descriptor.getElementName_za3lpa$(index);
    return true;
  };
  DynamicObjectEncoder.prototype.encodeValue_za3rmp$ = function (value) {
    if (this.currentElementIsMapKey_0) {
      this.currentName_0 = value.toString();
    } else if (this.isNotStructured_0()) {
      this.result = value;
    } else {
      this.current_0.jsObject[this.currentName_0] = value;
    }
  };
  DynamicObjectEncoder.prototype.encodeChar_s8itvh$ = function (value) {
    this.encodeValue_za3rmp$(String.fromCharCode(value));
  };
  DynamicObjectEncoder.prototype.encodeNull = function () {
    if (this.currentElementIsMapKey_0) {
      this.currentName_0 = null;
    } else {
      if (this.encodeNullAsUndefined_0)
        return;
      this.current_0.jsObject[this.currentName_0] = null;
    }
  };
  DynamicObjectEncoder.prototype.encodeEnum_szpzho$ = function (enumDescriptor, index) {
    this.encodeValue_za3rmp$(enumDescriptor.getElementName_za3lpa$(index));
  };
  DynamicObjectEncoder.prototype.encodeLong_s8cxhz$ = function (value) {
    var asDouble = value.toNumber();
    var conversionHasLossOfPrecision = JsMath.abs(asDouble) > MAX_SAFE_INTEGER;
    if (!this.json.configuration.isLenient && conversionHasLossOfPrecision) {
      throw IllegalArgumentException_init(value.toString() + " can't be serialized to number due to a potential precision loss. " + 'Use the JsonConfiguration option isLenient to serialize anyway');
    }if (this.currentElementIsMapKey_0 && conversionHasLossOfPrecision) {
      throw IllegalArgumentException_init('Long with value ' + value.toString() + " can't be used in json as map key, because its value is larger than Number.MAX_SAFE_INTEGER");
    }this.encodeValue_za3rmp$(asDouble);
  };
  DynamicObjectEncoder.prototype.encodeFloat_mx4ult$ = function (value) {
    this.encodeDouble_14dthe$(value);
  };
  DynamicObjectEncoder.prototype.encodeDouble_14dthe$ = function (value) {
    if (this.currentElementIsMapKey_0) {
      var hasNonZeroFractionalPart = JsMath.floor(value) !== value;
      if (!isFinite_0(value) || hasNonZeroFractionalPart) {
        throw IllegalArgumentException_init('Double with value ' + value + " can't be used in json as map key, because its value is not an integer.");
      }}this.encodeValue_za3rmp$(value);
  };
  DynamicObjectEncoder.prototype.encodeJsonElement_qiw0cd$ = function (element) {
    this.encodeSerializableValue_tf03ej$(JsonElementSerializer_getInstance(), element);
  };
  DynamicObjectEncoder.prototype.shouldEncodeElementDefault_szpzho$ = function (descriptor, index) {
    return this.json.configuration.encodeDefaults;
  };
  DynamicObjectEncoder.prototype.enterNode_0 = function (jsObject, writeMode) {
    var child = new DynamicObjectEncoder$Node(writeMode, jsObject);
    child.parent = this.current_0;
    this.current_0 = child;
  };
  DynamicObjectEncoder.prototype.exitNode_0 = function () {
    this.current_0 = this.current_0.parent;
    this.currentElementIsMapKey_0 = false;
  };
  DynamicObjectEncoder.prototype.isNotStructured_0 = function () {
    return this.result === DynamicObjectEncoder$NoOutputMark_getInstance();
  };
  DynamicObjectEncoder.prototype.encodeSerializableValue_tf03ej$ = function (serializer, value) {
    encodePolymorphically$break: do {
      var tmp$, tmp$_0;
      if (!Kotlin.isType(serializer, AbstractPolymorphicSerializer) || this.json.configuration.useArrayPolymorphism) {
        serializer.serialize_55azsf$(this, value);
        break encodePolymorphically$break;
      }var actualSerializer = findActualSerializer(this, Kotlin.isType(tmp$ = serializer, SerializationStrategy) ? tmp$ : throwCCE(), Kotlin.isType(tmp$_0 = value, Any) ? tmp$_0 : throwCCE());
      this.writePolymorphic_0 = true;
      actualSerializer.serialize_55azsf$(this, value);
    }
     while (false);
  };
  DynamicObjectEncoder.prototype.beginStructure_24f42q$ = function (descriptor) {
    if (this.currentElementIsMapKey_0) {
      throw IllegalArgumentException_init('Value of type ' + descriptor.serialName + " can't be used in json as map key. " + ('It should have either primitive or enum kind, but its kind is ' + descriptor.kind + '.'));
    }var newMode = this.selectMode_24f42q$(descriptor);
    if (this.result === DynamicObjectEncoder$NoOutputMark_getInstance()) {
      this.result = this.newChild_0(newMode);
      this.current_0 = new DynamicObjectEncoder$Node(newMode, this.result);
      this.current_0.parent = this.current_0;
    } else {
      var child = this.newChild_0(newMode);
      this.current_0.jsObject[this.currentName_0] = child;
      this.enterNode_0(child, newMode);
    }
    if (this.writePolymorphic_0) {
      this.writePolymorphic_0 = false;
      this.current_0.jsObject[this.json.configuration.classDiscriminator] = descriptor.serialName;
    }this.current_0.index = 0;
    return this;
  };
  DynamicObjectEncoder.prototype.newChild_0 = function (writeMode) {
    switch (writeMode.name) {
      case 'OBJ':
      case 'MAP':
        return {};
      case 'LIST':
        return [];
      default:return Kotlin.noWhenBranchMatched();
    }
  };
  DynamicObjectEncoder.prototype.endStructure_24f42q$ = function (descriptor) {
    this.exitNode_0();
  };
  DynamicObjectEncoder.prototype.selectMode_24f42q$ = function (desc) {
    var tmp$;
    tmp$ = desc.kind;
    if (equals(tmp$, StructureKind.CLASS) || equals(tmp$, StructureKind.OBJECT) || equals(tmp$, SerialKind.CONTEXTUAL))
      return DynamicObjectEncoder$WriteMode$OBJ_getInstance();
    else if (equals(tmp$, StructureKind.LIST) || Kotlin.isType(tmp$, PolymorphicKind))
      return DynamicObjectEncoder$WriteMode$LIST_getInstance();
    else if (equals(tmp$, StructureKind.MAP))
      return DynamicObjectEncoder$WriteMode$MAP_getInstance();
    else if (Kotlin.isType(tmp$, PrimitiveKind) || equals(tmp$, SerialKind.ENUM)) {
      throw IllegalStateException_init('DynamicObjectSerializer does not support serialization of singular primitive values or enum types.'.toString());
    } else
      return Kotlin.noWhenBranchMatched();
  };
  DynamicObjectEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DynamicObjectEncoder',
    interfaces: [JsonEncoder, AbstractEncoder]
  };
  function DynamicPrimitiveEncoder(json) {
    AbstractEncoder.call(this);
    this.json_kvyz8o$_0 = json;
    this.result = null;
  }
  Object.defineProperty(DynamicPrimitiveEncoder.prototype, 'json', {
    get: function () {
      return this.json_kvyz8o$_0;
    }
  });
  Object.defineProperty(DynamicPrimitiveEncoder.prototype, 'serializersModule', {
    configurable: true,
    get: function () {
      return this.json.serializersModule;
    }
  });
  DynamicPrimitiveEncoder.prototype.encodeNull = function () {
    this.result = null;
  };
  DynamicPrimitiveEncoder.prototype.encodeLong_s8cxhz$ = function (value) {
    var asDouble = value.toNumber();
    if (!this.json.configuration.isLenient && abs(value).toNumber() > MAX_SAFE_INTEGER) {
      throw IllegalArgumentException_init(value.toString() + " can't be deserialized to number due to a potential precision loss. " + 'Use the JsonConfiguration option isLenient to serialise anyway');
    }this.encodeValue_za3rmp$(asDouble);
  };
  DynamicPrimitiveEncoder.prototype.encodeChar_s8itvh$ = function (value) {
    this.encodeValue_za3rmp$(String.fromCharCode(value));
  };
  DynamicPrimitiveEncoder.prototype.encodeValue_za3rmp$ = function (value) {
    this.result = value;
  };
  DynamicPrimitiveEncoder.prototype.encodeEnum_szpzho$ = function (enumDescriptor, index) {
    this.encodeValue_za3rmp$(enumDescriptor.getElementName_za3lpa$(index));
  };
  DynamicPrimitiveEncoder.prototype.endStructure_24f42q$ = function (descriptor) {
  };
  DynamicPrimitiveEncoder.prototype.encodeJsonElement_qiw0cd$ = function (element) {
    this.encodeSerializableValue_tf03ej$(JsonElementSerializer_getInstance(), element);
  };
  DynamicPrimitiveEncoder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'DynamicPrimitiveEncoder',
    interfaces: [JsonEncoder, AbstractEncoder]
  };
  function JsonStringBuilder() {
    this.sb_0 = StringBuilder_init_0(128);
  }
  JsonStringBuilder.prototype.append_s8cxhz$ = function (value) {
    this.sb_0.append_s8jyv4$(value);
  };
  JsonStringBuilder.prototype.append_s8itvh$ = function (ch) {
    this.sb_0.append_s8itvh$(ch);
  };
  JsonStringBuilder.prototype.append_61zpoe$ = function (string) {
    this.sb_0.append_pdl1vj$(string);
  };
  JsonStringBuilder.prototype.appendQuoted_61zpoe$ = function (string) {
    printQuoted(this.sb_0, string);
  };
  JsonStringBuilder.prototype.toString = function () {
    return this.sb_0.toString();
  };
  JsonStringBuilder.prototype.release = function () {
  };
  JsonStringBuilder.$metadata$ = {
    kind: Kind_CLASS,
    simpleName: 'JsonStringBuilder',
    interfaces: []
  };
  var package$kotlinx = _.kotlinx || (_.kotlinx = {});
  var package$serialization = package$kotlinx.serialization || (package$kotlinx.serialization = {});
  var package$json = package$serialization.json || (package$serialization.json = {});
  var package$internal = package$json.internal || (package$json.internal = {});
  package$internal.Composer = Composer;
  package$internal.ComposerForUnsignedNumbers = ComposerForUnsignedNumbers;
  package$internal.JsonException = JsonException;
  package$internal.JsonDecodingException = JsonDecodingException;
  package$internal.JsonDecodingException_f0n09d$ = JsonDecodingException_0;
  package$internal.JsonEncodingException = JsonEncodingException;
  package$internal.JsonDecodingException_kx378j$ = JsonDecodingException_1;
  package$internal.InvalidFloatingPointEncoded_qwibp1$ = InvalidFloatingPointEncoded;
  package$internal.InvalidFloatingPointEncoded_x0xb19$ = InvalidFloatingPointEncoded_0;
  package$internal.InvalidFloatingPointDecoded_x0xb19$ = InvalidFloatingPointDecoded;
  package$internal.throwInvalidFloatingPointDecoded_gle6j2$ = throwInvalidFloatingPointDecoded;
  package$internal.UnknownKeyException_wdz5eb$ = UnknownKeyException;
  package$internal.InvalidKeyKindException_jo479d$ = InvalidKeyKindException;
  Object.defineProperty(package$internal, 'lenientHint_8be2vx$', {
    get: function () {
      return lenientHint;
    }
  });
  Object.defineProperty(package$internal, 'coerceInputValuesHint_8be2vx$', {
    get: function () {
      return coerceInputValuesHint;
    }
  });
  Object.defineProperty(package$internal, 'specialFlowingValuesHint_8be2vx$', {
    get: function () {
      return specialFlowingValuesHint;
    }
  });
  Object.defineProperty(package$internal, 'ignoreUnknownKeysHint_8be2vx$', {
    get: function () {
      return ignoreUnknownKeysHint;
    }
  });
  Object.defineProperty(package$internal, 'allowStructuredMapKeysHint_8be2vx$', {
    get: function () {
      return allowStructuredMapKeysHint;
    }
  });
  Object.defineProperty(package$internal, 'NULL_8be2vx$', {
    get: function () {
      return NULL;
    }
  });
  Object.defineProperty(package$internal, 'COMMA_8be2vx$', {
    get: function () {
      return COMMA;
    }
  });
  Object.defineProperty(package$internal, 'COLON_8be2vx$', {
    get: function () {
      return COLON;
    }
  });
  Object.defineProperty(package$internal, 'BEGIN_OBJ_8be2vx$', {
    get: function () {
      return BEGIN_OBJ;
    }
  });
  Object.defineProperty(package$internal, 'END_OBJ_8be2vx$', {
    get: function () {
      return END_OBJ;
    }
  });
  Object.defineProperty(package$internal, 'BEGIN_LIST_8be2vx$', {
    get: function () {
      return BEGIN_LIST;
    }
  });
  Object.defineProperty(package$internal, 'END_LIST_8be2vx$', {
    get: function () {
      return END_LIST;
    }
  });
  Object.defineProperty(package$internal, 'STRING_8be2vx$', {
    get: function () {
      return STRING;
    }
  });
  Object.defineProperty(package$internal, 'STRING_ESC_8be2vx$', {
    get: function () {
      return STRING_ESC;
    }
  });
  Object.defineProperty(package$internal, 'INVALID_8be2vx$', {
    get: function () {
      return INVALID;
    }
  });
  Object.defineProperty(package$internal, 'UNICODE_ESC_8be2vx$', {
    get: function () {
      return UNICODE_ESC;
    }
  });
  Object.defineProperty(package$internal, 'TC_OTHER_8be2vx$', {
    get: function () {
      return TC_OTHER;
    }
  });
  Object.defineProperty(package$internal, 'TC_STRING_8be2vx$', {
    get: function () {
      return TC_STRING;
    }
  });
  Object.defineProperty(package$internal, 'TC_STRING_ESC_8be2vx$', {
    get: function () {
      return TC_STRING_ESC;
    }
  });
  Object.defineProperty(package$internal, 'TC_WHITESPACE_8be2vx$', {
    get: function () {
      return TC_WHITESPACE;
    }
  });
  Object.defineProperty(package$internal, 'TC_COMMA_8be2vx$', {
    get: function () {
      return TC_COMMA;
    }
  });
  Object.defineProperty(package$internal, 'TC_COLON_8be2vx$', {
    get: function () {
      return TC_COLON;
    }
  });
  Object.defineProperty(package$internal, 'TC_BEGIN_OBJ_8be2vx$', {
    get: function () {
      return TC_BEGIN_OBJ;
    }
  });
  Object.defineProperty(package$internal, 'TC_END_OBJ_8be2vx$', {
    get: function () {
      return TC_END_OBJ;
    }
  });
  Object.defineProperty(package$internal, 'TC_BEGIN_LIST_8be2vx$', {
    get: function () {
      return TC_BEGIN_LIST;
    }
  });
  Object.defineProperty(package$internal, 'TC_END_LIST_8be2vx$', {
    get: function () {
      return TC_END_LIST;
    }
  });
  Object.defineProperty(package$internal, 'TC_EOF_8be2vx$', {
    get: function () {
      return TC_EOF;
    }
  });
  Object.defineProperty(package$internal, 'TC_INVALID_8be2vx$', {
    get: function () {
      return TC_INVALID;
    }
  });
  Object.defineProperty(package$internal, 'CharMappings', {
    get: CharMappings_getInstance
  });
  package$internal.charToTokenClass_8e8zqy$ = charToTokenClass;
  package$internal.escapeToChar_kcn2v3$ = escapeToChar;
  $$importsForInline$$['kotlinx-serialization-kotlinx-serialization-json-js-legacy'] = _;
  package$internal.JsonLexer = JsonLexer;
  Object.defineProperty(package$internal, 'JsonAlternativeNamesKey_8be2vx$', {
    get: function () {
      return JsonAlternativeNamesKey;
    }
  });
  package$internal.buildAlternativeNamesMap_tie8r4$ = buildAlternativeNamesMap;
  package$internal.getJsonNameIndex_bt3nd1$ = getJsonNameIndex;
  package$internal.getJsonNameIndexOrThrow_bt3nd1$ = getJsonNameIndexOrThrow;
  package$internal.JsonTreeReader = JsonTreeReader;
  $$importsForInline$$['kotlinx-serialization-kotlinx-serialization-core-js-legacy'] = $module$kotlinx_serialization_kotlinx_serialization_core_js_legacy;
  package$internal.encodePolymorphically_51w9c5$ = encodePolymorphically;
  package$internal.checkKind_x1ow4c$ = checkKind;
  package$internal.decodeSerializableValuePolymorphic_3uiavc$ = decodeSerializableValuePolymorphic;
  package$internal.PolymorphismValidator = PolymorphismValidator;
  DescriptorSchemaCache.Key = DescriptorSchemaCache$Key;
  package$internal.DescriptorSchemaCache = DescriptorSchemaCache;
  package$internal.StreamingJsonDecoder = StreamingJsonDecoder;
  package$internal.JsonDecoderForUnsignedTypes = JsonDecoderForUnsignedTypes;
  package$internal.get_isUnsignedNumber_tie8r4$ = get_isUnsignedNumber;
  package$internal.StreamingJsonEncoder_init_ps2ywi$ = StreamingJsonEncoder_init;
  package$internal.StreamingJsonEncoder = StreamingJsonEncoder;
  Object.defineProperty(package$internal, 'ESCAPE_STRINGS_8be2vx$', {
    get: function () {
      return ESCAPE_STRINGS;
    }
  });
  Object.defineProperty(package$internal, 'ESCAPE_MARKERS_8be2vx$', {
    get: function () {
      return ESCAPE_MARKERS;
    }
  });
  package$internal.printQuoted_jigvc$ = printQuoted;
  package$internal.toBooleanStrictOrNull_7efafi$ = toBooleanStrictOrNull;
  package$internal.readJson_ijhaef$ = readJson;
  package$internal.readPolymorphicJson_nnpnh6$ = readPolymorphicJson;
  package$internal.writeJson_4dixew$ = writeJson;
  Object.defineProperty(package$internal, 'PRIMITIVE_TAG_8be2vx$', {
    get: function () {
      return PRIMITIVE_TAG;
    }
  });
  Object.defineProperty(WriteMode, 'OBJ', {
    get: WriteMode$OBJ_getInstance
  });
  Object.defineProperty(WriteMode, 'LIST', {
    get: WriteMode$LIST_getInstance
  });
  Object.defineProperty(WriteMode, 'MAP', {
    get: WriteMode$MAP_getInstance
  });
  Object.defineProperty(WriteMode, 'POLY_OBJ', {
    get: WriteMode$POLY_OBJ_getInstance
  });
  package$internal.WriteMode = WriteMode;
  package$internal.switchMode_q3nfcb$ = switchMode;
  package$internal.get_carrierDescriptor_tie8r4$ = get_carrierDescriptor;
  package$internal.selectMapMode_clqyd9$ = selectMapMode;
  Object.defineProperty(Json, 'Default', {
    get: Json$Default_getInstance
  });
  package$json.Json = Json;
  package$json.Json_x26noe$ = Json_0;
  package$json.JsonBuilder = JsonBuilder;
  package$json.JsonConfiguration = JsonConfiguration;
  package$json.JsonContentPolymorphicSerializer = JsonContentPolymorphicSerializer;
  package$json.JsonDecoder = JsonDecoder;
  Object.defineProperty(JsonElement, 'Companion', {
    get: JsonElement$Companion_getInstance
  });
  package$json.JsonElement = JsonElement;
  Object.defineProperty(JsonPrimitive, 'Companion', {
    get: JsonPrimitive$Companion_getInstance
  });
  package$json.JsonPrimitive = JsonPrimitive;
  package$json.JsonPrimitive_1v8dbw$ = JsonPrimitive_0;
  package$json.JsonPrimitive_rcaewn$ = JsonPrimitive_1;
  package$json.JsonPrimitive_pdl1vj$ = JsonPrimitive_2;
  package$json.JsonLiteral = JsonLiteral;
  Object.defineProperty(package$json, 'JsonNull', {
    get: JsonNull_getInstance
  });
  Object.defineProperty(JsonObject, 'Companion', {
    get: JsonObject$Companion_getInstance
  });
  package$json.JsonObject = JsonObject;
  Object.defineProperty(JsonArray, 'Companion', {
    get: JsonArray$Companion_getInstance
  });
  package$json.JsonArray = JsonArray;
  package$json.get_jsonPrimitive_u3sd3g$ = get_jsonPrimitive;
  package$json.get_jsonObject_u3sd3g$ = get_jsonObject;
  package$json.get_jsonArray_u3sd3g$ = get_jsonArray;
  package$json.get_jsonNull_u3sd3g$ = get_jsonNull;
  package$json.get_int_59esu7$ = get_int;
  package$json.get_intOrNull_59esu7$ = get_intOrNull;
  package$json.get_long_59esu7$ = get_long;
  package$json.get_longOrNull_59esu7$ = get_longOrNull;
  package$json.get_double_59esu7$ = get_double;
  package$json.get_doubleOrNull_59esu7$ = get_doubleOrNull;
  package$json.get_float_59esu7$ = get_float;
  package$json.get_floatOrNull_59esu7$ = get_floatOrNull;
  package$json.get_boolean_59esu7$ = get_boolean;
  package$json.get_booleanOrNull_59esu7$ = get_booleanOrNull;
  package$json.get_contentOrNull_59esu7$ = get_contentOrNull;
  package$json.unexpectedJson_puj7f4$ = unexpectedJson;
  package$json.buildJsonObject_s5o6vg$ = buildJsonObject;
  package$json.buildJsonArray_mb52fq$ = buildJsonArray;
  package$json.JsonObjectBuilder = JsonObjectBuilder;
  package$json.putJsonObject_qdwamy$ = putJsonObject;
  package$json.putJsonArray_h6ak7k$ = putJsonArray;
  package$json.put_45j0gm$ = put;
  package$json.put_iet0pv$ = put_0;
  package$json.put_kdidqz$ = put_1;
  package$json.JsonArrayBuilder = JsonArrayBuilder;
  package$json.add_2ifez4$ = add;
  package$json.add_imjofx$ = add_0;
  package$json.add_kl91h1$ = add_1;
  package$json.addJsonObject_d58nnk$ = addJsonObject;
  package$json.addJsonArray_t23uk6$ = addJsonArray;
  package$json.JsonDslMarker = JsonDslMarker;
  Object.defineProperty(package$json, 'JsonElementSerializer', {
    get: JsonElementSerializer_getInstance
  });
  Object.defineProperty(package$json, 'JsonPrimitiveSerializer', {
    get: JsonPrimitiveSerializer_getInstance
  });
  Object.defineProperty(package$json, 'JsonNullSerializer', {
    get: JsonNullSerializer_getInstance
  });
  Object.defineProperty(package$json, 'JsonObjectSerializer', {
    get: JsonObjectSerializer_getInstance
  });
  Object.defineProperty(package$json, 'JsonArraySerializer', {
    get: JsonArraySerializer_getInstance
  });
  package$json.asJsonDecoder_k984l0$ = asJsonDecoder;
  package$json.asJsonEncoder_vwx09w$ = asJsonEncoder;
  package$json.JsonEncoder = JsonEncoder;
  package$json.JsonNames = JsonNames;
  package$json.JsonTransformingSerializer = JsonTransformingSerializer;
  package$json.decodeFromDynamic_76vv94$ = decodeFromDynamic;
  package$json.encodeToDynamic_9ibeu3$ = encodeToDynamic;
  package$internal.createMapForCache_v5nmvw$ = createMapForCache;
  Object.defineProperty(package$internal, 'MAX_SAFE_INTEGER_8be2vx$', {
    get: function () {
      return MAX_SAFE_INTEGER;
    }
  });
  package$internal.decodeDynamic = decodeDynamic;
  package$internal.encodeToDynamic = encodeDynamic;
  package$internal.JsonStringBuilder = JsonStringBuilder;
  PolymorphismValidator.prototype.contextual_cfhkba$ = SerializersModuleCollector.prototype.contextual_cfhkba$;
  JsonDecoder.prototype.decodeNullableSerializableValue_aae3ea$ = Decoder.prototype.decodeNullableSerializableValue_aae3ea$;
  JsonDecoder.prototype.decodeSerializableValue_w63s0f$ = Decoder.prototype.decodeSerializableValue_w63s0f$;
  JsonDecoder.prototype.decodeCollectionSize_24f42q$ = CompositeDecoder.prototype.decodeCollectionSize_24f42q$;
  JsonDecoder.prototype.decodeSequentially = CompositeDecoder.prototype.decodeSequentially;
  JsonDecoder.prototype.decodeNullableSerializableElement_8viuyw$ = CompositeDecoder.prototype.decodeNullableSerializableElement_8viuyw$;
  JsonDecoder.prototype.decodeSerializableElement_12e8id$ = CompositeDecoder.prototype.decodeSerializableElement_12e8id$;
  StreamingJsonDecoder.prototype.decodeNullableSerializableValue_aae3ea$ = JsonDecoder.prototype.decodeNullableSerializableValue_aae3ea$;
  StreamingJsonDecoder.prototype.decodeCollectionSize_24f42q$ = JsonDecoder.prototype.decodeCollectionSize_24f42q$;
  StreamingJsonDecoder.prototype.decodeSequentially = JsonDecoder.prototype.decodeSequentially;
  JsonEncoder.prototype.beginCollection_szpzho$ = Encoder.prototype.beginCollection_szpzho$;
  JsonEncoder.prototype.encodeNotNullMark = Encoder.prototype.encodeNotNullMark;
  JsonEncoder.prototype.encodeNullableSerializableValue_f4686g$ = Encoder.prototype.encodeNullableSerializableValue_f4686g$;
  JsonEncoder.prototype.encodeSerializableValue_tf03ej$ = Encoder.prototype.encodeSerializableValue_tf03ej$;
  JsonEncoder.prototype.shouldEncodeElementDefault_szpzho$ = CompositeEncoder.prototype.shouldEncodeElementDefault_szpzho$;
  StreamingJsonEncoder.prototype.beginCollection_szpzho$ = JsonEncoder.prototype.beginCollection_szpzho$;
  StreamingJsonEncoder.prototype.encodeNotNullMark = JsonEncoder.prototype.encodeNotNullMark;
  StreamingJsonEncoder.prototype.encodeNullableSerializableValue_f4686g$ = JsonEncoder.prototype.encodeNullableSerializableValue_f4686g$;
  Object.defineProperty(defer$ObjectLiteral.prototype, 'annotations', Object.getOwnPropertyDescriptor(SerialDescriptor.prototype, 'annotations'));
  Object.defineProperty(defer$ObjectLiteral.prototype, 'isInline', Object.getOwnPropertyDescriptor(SerialDescriptor.prototype, 'isInline'));
  Object.defineProperty(defer$ObjectLiteral.prototype, 'isNullable', Object.getOwnPropertyDescriptor(SerialDescriptor.prototype, 'isNullable'));
  lenientHint = "Use 'isLenient = true' in 'Json {}` builder to accept non-compliant JSON.";
  coerceInputValuesHint = "Use 'coerceInputValues = true' in 'Json {}` builder to coerce nulls to default values.";
  specialFlowingValuesHint = "It is possible to deserialize them using 'JsonBuilder.allowSpecialFloatingPointValues = true'";
  ignoreUnknownKeysHint = "Use 'ignoreUnknownKeys = true' in 'Json {}' builder to ignore unknown keys.";
  allowStructuredMapKeysHint = "Use 'allowStructuredMapKeys = true' in 'Json {}' builder to convert such maps to [key1, value1, key2, value2,...] arrays.";
  NULL = 'null';
  COMMA = 44;
  COLON = 58;
  BEGIN_OBJ = 123;
  END_OBJ = 125;
  BEGIN_LIST = 91;
  END_LIST = 93;
  STRING = 34;
  STRING_ESC = 92;
  INVALID = toChar(0);
  UNICODE_ESC = 117;
  TC_OTHER = 0;
  TC_STRING = 1;
  TC_STRING_ESC = 2;
  TC_WHITESPACE = 3;
  TC_COMMA = 4;
  TC_COLON = 5;
  TC_BEGIN_OBJ = 6;
  TC_END_OBJ = 7;
  TC_BEGIN_LIST = 8;
  TC_END_LIST = 9;
  TC_EOF = 10;
  TC_INVALID = kotlin_js_internal_ByteCompanionObject.MAX_VALUE;
  CTC_MAX = 126;
  ESC2C_MAX = 117;
  asciiCaseMask = 32;
  JsonAlternativeNamesKey = new DescriptorSchemaCache$Key();
  unsignedNumberDescriptors = setOf([serializer(UInt_init.Companion).descriptor, serializer_0(ULong_init.Companion).descriptor, serializer_1(UByte_init.Companion).descriptor, serializer_2(UShort_init.Companion).descriptor]);
  var $receiver = Kotlin.newArray(93, null);
  for (var c = 0; c <= 31; c++) {
    var c1 = toHexChar(c >> 12);
    var c2 = toHexChar(c >> 8);
    var c3 = toHexChar(c >> 4);
    var c4 = toHexChar(c);
    $receiver[c] = '\\' + 'u' + String.fromCharCode(c1) + String.fromCharCode(c2) + String.fromCharCode(c3) + String.fromCharCode(c4);
  }
  $receiver[34] = '\\"';
  $receiver[92] = '\\\\';
  $receiver[9] = '\\t';
  $receiver[8] = '\\b';
  $receiver[10] = '\\n';
  $receiver[13] = '\\r';
  $receiver[12] = '\\f';
  ESCAPE_STRINGS = $receiver;
  var $receiver_0 = new Int8Array(93);
  for (var c_0 = 0; c_0 <= 31; c_0++) {
    $receiver_0[c_0] = toByte(1);
  }
  $receiver_0[34] = toByte(34);
  $receiver_0[92] = toByte(92);
  $receiver_0[9] = toByte(116);
  $receiver_0[8] = toByte(98);
  $receiver_0[10] = toByte(110);
  $receiver_0[13] = toByte(114);
  $receiver_0[12] = toByte(102);
  ESCAPE_MARKERS = $receiver_0;
  PRIMITIVE_TAG = 'primitive';
  defaultIndent = '    ';
  defaultDiscriminator = 'type';
  infixToDeprecated = "Infix 'to' operator is deprecated for removal for the favour of 'add'";
  unaryPlusDeprecated = "Unary plus is deprecated for removal for the favour of 'add'";
  MAX_SAFE_INTEGER = L9007199254740991.toNumber();
  Kotlin.defineModule('kotlinx-serialization-kotlinx-serialization-json-js-legacy', _);
  return _;
}));

//# sourceMappingURL=kotlinx-serialization-kotlinx-serialization-json-js-legacy.js.map
