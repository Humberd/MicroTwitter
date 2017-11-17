package pl.bas.microtwitter.serializers

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*


class DateDeserializer : JsonDeserializer<Date> {

    override fun deserialize(json: JsonElement?, typeOfT: Type,
                             context: JsonDeserializationContext): Date? {
        return if (json == null) null else Date(json.asLong)
    }
}

class DateSerializer : JsonSerializer<Date> {
    override fun serialize(date: Date?, typeOfSrc: Type,
                           context: JsonSerializationContext): JsonElement? {
        return if (date == null) null else JsonPrimitive(date!!.getTime())
    }
}