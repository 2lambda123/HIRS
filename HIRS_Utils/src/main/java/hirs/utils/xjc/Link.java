//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.02.16 at 06:40:19 PM UTC 
//


package hirs.utils.xjc;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Link complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Link">
 *   &lt;complexContent>
 *     &lt;extension base="{http://standards.iso.org/iso/19770/-2/2015/schema.xsd}BaseElement">
 *       &lt;attribute name="artifact" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="href" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="media" type="{http://standards.iso.org/iso/19770/-2/2015/schema.xsd}Media" />
 *       &lt;attribute name="ownership" type="{http://standards.iso.org/iso/19770/-2/2015/schema.xsd}Ownership" />
 *       &lt;attribute name="rel" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="type" type="{http://standards.iso.org/iso/19770/-2/2015/schema.xsd}MediaType" />
 *       &lt;attribute name="use" type="{http://standards.iso.org/iso/19770/-2/2015/schema.xsd}Use" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Link", namespace = "http://standards.iso.org/iso/19770/-2/2015/schema.xsd")
public class Link
    extends BaseElement
{

    @XmlAttribute(name = "artifact")
    protected String artifact;
    @XmlAttribute(name = "href", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "media")
    protected String media;
    @XmlAttribute(name = "ownership")
    protected Ownership ownership;
    @XmlAttribute(name = "rel", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String rel;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "use")
    protected Use use;

    /**
     * Gets the value of the artifact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArtifact() {
        return artifact;
    }

    /**
     * Sets the value of the artifact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArtifact(String value) {
        this.artifact = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the media property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMedia() {
        return media;
    }

    /**
     * Sets the value of the media property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMedia(String value) {
        this.media = value;
    }

    /**
     * Gets the value of the ownership property.
     * 
     * @return
     *     possible object is
     *     {@link Ownership }
     *     
     */
    public Ownership getOwnership() {
        return ownership;
    }

    /**
     * Sets the value of the ownership property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ownership }
     *     
     */
    public void setOwnership(Ownership value) {
        this.ownership = value;
    }

    /**
     * Gets the value of the rel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRel() {
        return rel;
    }

    /**
     * Sets the value of the rel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRel(String value) {
        this.rel = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the use property.
     * 
     * @return
     *     possible object is
     *     {@link Use }
     *     
     */
    public Use getUse() {
        return use;
    }

    /**
     * Sets the value of the use property.
     * 
     * @param value
     *     allowed object is
     *     {@link Use }
     *     
     */
    public void setUse(Use value) {
        this.use = value;
    }

}
