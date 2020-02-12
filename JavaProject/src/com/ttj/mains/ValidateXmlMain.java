package com.ttj.mains;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class ValidateXmlMain {
    public static void main(String[] args){
        String xsdSchemaLoc = "/Users/ttj/My_blogs_workspace/resources/hello-service-schema.xsd";
        String xmlFileLoc = "/Users/ttj/My_blogs_workspace/resources/hello-service-request.xml";

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdSchemaLoc));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFileLoc)));
            System.out.println("Validated successfully.");
        }catch(Exception e){
            System.out.println("Validation failed...");
            e.printStackTrace();
        }
    }

}
