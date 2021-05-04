package com.mnl.emanuel.serializer;

import java.lang.reflect.Field;

public final class Serializer {

    public static void main(String[] args) {
        try {
            Serializer serializer = new Serializer();
            MyDataClass myDataObject = new MyDataClass(true, 2020, "FFHS");
            serializer.serializeAllFields(myDataObject);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void serializeAllFields(Object dataObject) throws Exception {
       Field[] declaredFields = dataObject.getClass().getDeclaredFields();
       
       for (int i = 0; i < declaredFields.length; i++) {
    	   Field field = declaredFields[i];
    	   field.setAccessible(true);  
    	   
    	   if (field.getDeclaredAnnotation(DoNotSerialize.class) == null) {
	    	   String fieldName   = field.getName();                   	// get attribute name
	    	   Class<?> fieldType = field.getType();					// get field data type
	    	   Object fieldValue = field.get(dataObject);				// get field value
	    	   printFieldInfo(fieldName, fieldType, fieldValue);
    	   }
       }
    }

    private void printFieldInfo(String fieldName, Class<?> fieldType, Object fieldValue) {
        System.out.format("%s : %s = %s%n", fieldName, fieldType, fieldValue);
    }
}