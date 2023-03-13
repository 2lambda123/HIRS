//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.02.16 at 06:40:19 PM UTC 
//


package hirs.utils.xjc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Use.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Use">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="required"/>
 *     &lt;enumeration value="recommended"/>
 *     &lt;enumeration value="optional"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Use", namespace = "http://standards.iso.org/iso/19770/-2/2015/schema.xsd")
@XmlEnum
public enum Use {


    /**
     * 
     *             The [Link]'d software is absolutely required for installation
     *           
     * 
     */
    @XmlEnumValue("required")
    REQUIRED("required"),

    /**
     * 
     *             Not absolutely required, but install unless directed not to
     *           
     * 
     */
    @XmlEnumValue("recommended")
    RECOMMENDED("recommended"),

    /**
     * 
     *             Not absolutely required, install only when asked
     *           
     * 
     */
    @XmlEnumValue("optional")
    OPTIONAL("optional");
    private final String value;

    Use(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Use fromValue(String v) {
        for (Use c: Use.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
