package ml.volder.transporter.jsonmanager;

import com.google.gson.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Data {
    private JsonObject data;

    public Data() {
        this.data = new JsonObject();
    }

    public JsonObject getData() {
        return this.data;
    }

    public <T> T getEntry(String jsonPath, Class<T> expectedReturnType) {
        if(!getData().has(jsonPath))
            return null;
        JsonElement element = getData().get(jsonPath);
        if(expectedReturnType.equals(Integer.class)){
            Integer returnValue = element.getAsInt();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Boolean.class)){
            Boolean returnValue = element.getAsBoolean();
            return (T) returnValue;
        }else if(expectedReturnType.equals(String.class)){
            String returnValue = element.getAsString();
            return (T) returnValue;
        }else if(expectedReturnType.equals(BigDecimal.class)){
            BigDecimal returnValue = element.getAsBigDecimal();
            return (T) returnValue;
        }else if(expectedReturnType.equals(BigInteger.class)){
            BigInteger returnValue = element.getAsBigInteger();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Character.class)){
            Character returnValue = element.getAsCharacter();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Double.class)){
            Double returnValue = element.getAsDouble();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Float.class)){
            Float returnValue = element.getAsFloat();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Long.class)){
            Long returnValue = element.getAsLong();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Number.class)){
            Number returnValue = element.getAsNumber();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Short.class)){
            Short returnValue = element.getAsShort();
            return (T) returnValue;
        }else if(expectedReturnType.equals(JsonObject.class)){
            JsonObject returnValue = element.getAsJsonObject();
            return (T) returnValue;
        }else if(expectedReturnType.equals(Byte.class)){
            Byte returnValue = element.getAsByte();
            return (T) returnValue;
        }else if(expectedReturnType.equals(JsonArray.class)){
            JsonArray returnValue = element.getAsJsonArray();
            return (T) returnValue;
        }else if(expectedReturnType.equals(JsonNull.class)){
            JsonNull returnValue = element.getAsJsonNull();
            return (T) returnValue;
        }else if(expectedReturnType.equals(JsonPrimitive.class)){
            JsonPrimitive returnValue = element.getAsJsonPrimitive();
            return (T) returnValue;
        }
        return null;
    }
}