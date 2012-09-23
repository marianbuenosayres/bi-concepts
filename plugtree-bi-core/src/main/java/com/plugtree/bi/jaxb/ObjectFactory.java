//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.21 at 07:37:33 PM ART 
//


package com.plugtree.bi.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.plugtree.bi.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Definitions_QNAME = new QName("http://www.plugtree.com/spec/EPMN/20120921/MODEL", "definitions");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.plugtree.bi.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DiagramPlaneType }
     * 
     */
    public DiagramPlaneType createDiagramPlaneType() {
        return new DiagramPlaneType();
    }

    /**
     * Create an instance of {@link NetworkConnectorType }
     * 
     */
    public NetworkConnectorType createNetworkConnectorType() {
        return new NetworkConnectorType();
    }

    /**
     * Create an instance of {@link NetworkType }
     * 
     */
    public NetworkType createNetworkType() {
        return new NetworkType();
    }

    /**
     * Create an instance of {@link ImportType }
     * 
     */
    public ImportType createImportType() {
        return new ImportType();
    }

    /**
     * Create an instance of {@link DiagramShapeType }
     * 
     */
    public DiagramShapeType createDiagramShapeType() {
        return new DiagramShapeType();
    }

    /**
     * Create an instance of {@link NetworkProducerType }
     * 
     */
    public NetworkProducerType createNetworkProducerType() {
        return new NetworkProducerType();
    }

    /**
     * Create an instance of {@link NetworkEventNodeType }
     * 
     */
    public NetworkEventNodeType createNetworkEventNodeType() {
        return new NetworkEventNodeType();
    }

    /**
     * Create an instance of {@link NetworkConsumerType }
     * 
     */
    public NetworkConsumerType createNetworkConsumerType() {
        return new NetworkConsumerType();
    }

    /**
     * Create an instance of {@link NetworkExpressionType }
     * 
     */
    public NetworkExpressionType createNetworkExpressionType() {
        return new NetworkExpressionType();
    }

    /**
     * Create an instance of {@link DiagramBoundsType }
     * 
     */
    public DiagramBoundsType createDiagramBoundsType() {
        return new DiagramBoundsType();
    }

    /**
     * Create an instance of {@link DefinitionsType }
     * 
     */
    public DefinitionsType createDefinitionsType() {
        return new DefinitionsType();
    }

    /**
     * Create an instance of {@link DiagramEdgeType }
     * 
     */
    public DiagramEdgeType createDiagramEdgeType() {
        return new DiagramEdgeType();
    }

    /**
     * Create an instance of {@link NetworkProcessorType }
     * 
     */
    public NetworkProcessorType createNetworkProcessorType() {
        return new NetworkProcessorType();
    }

    /**
     * Create an instance of {@link NetworkChannelType }
     * 
     */
    public NetworkChannelType createNetworkChannelType() {
        return new NetworkChannelType();
    }

    /**
     * Create an instance of {@link DiagramPointType }
     * 
     */
    public DiagramPointType createDiagramPointType() {
        return new DiagramPointType();
    }

    /**
     * Create an instance of {@link NetworkRepositoryType }
     * 
     */
    public NetworkRepositoryType createNetworkRepositoryType() {
        return new NetworkRepositoryType();
    }

    /**
     * Create an instance of {@link DiagramType }
     * 
     */
    public DiagramType createDiagramType() {
        return new DiagramType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefinitionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.plugtree.com/spec/EPMN/20120921/MODEL", name = "definitions")
    public JAXBElement<DefinitionsType> createDefinitions(DefinitionsType value) {
        return new JAXBElement<DefinitionsType>(_Definitions_QNAME, DefinitionsType.class, null, value);
    }

}
