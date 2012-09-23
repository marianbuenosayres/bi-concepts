package com.plugtree.bi.parser;

import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.plugtree.bi.jaxb.DefinitionsType;
import com.plugtree.bi.jaxb.DiagramBoundsType;
import com.plugtree.bi.jaxb.DiagramEdgeType;
import com.plugtree.bi.jaxb.DiagramPlaneType;
import com.plugtree.bi.jaxb.DiagramPointType;
import com.plugtree.bi.jaxb.DiagramShapeType;
import com.plugtree.bi.jaxb.DiagramType;
import com.plugtree.bi.jaxb.NetworkConnectorType;
import com.plugtree.bi.jaxb.NetworkConsumerType;
import com.plugtree.bi.jaxb.NetworkExpressionType;
import com.plugtree.bi.jaxb.NetworkProducerType;
import com.plugtree.bi.jaxb.NetworkType;
import com.plugtree.bi.jaxb.ObjectFactory;
import com.plugtree.bi.xml.NetworkDef;

public class EPMNParser {

	private DefinitionsType parse(InputStream in) throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance("com.plugtree.bi.jaxb");
		Unmarshaller unm = ctx.createUnmarshaller();
		Object obj = unm.unmarshal(in);
		DefinitionsType retval = (DefinitionsType) obj;
		return retval;
	}
	
	NetworkDef createNetwork(InputStream in) throws Exception {
		DefinitionsType defType = parse(in);
		NetworkType netType = defType.getNetwork();
		DiagramType diaType = defType.getDiagram();
		NetworkDef def = new NetworkDef();
		netType.getProducer();
		netType.getConnector();
		netType.getConsumer();
		netType.getProcessor();
		netType.getProducer();
		
		netType.getChannel();
		List<NetworkConnectorType> conns = netType.getConnector();
		
		//TODO
		return def;
	}
	
	public static void main(String[] args) {
		DefinitionsType defType = new DefinitionsType();
		DiagramType diaType = new DiagramType();
		DiagramPlaneType plane = new DiagramPlaneType();
		plane.setElement("myNetwork");
		DiagramEdgeType edge = new DiagramEdgeType();
		edge.setElement("0xc00001");
		DiagramPointType point = new DiagramPointType();
		point.setX(BigInteger.ONE);
		point.setY(BigInteger.TEN);
		DiagramPointType point2 = new DiagramPointType();
		point2.setX(BigInteger.TEN);
		point2.setY(BigInteger.ZERO);
		DiagramShapeType shape1 = new DiagramShapeType();
		shape1.setElement("0xa00001");
		DiagramBoundsType bound1 = new DiagramBoundsType();
		bound1.setHeight(BigInteger.ONE);
		bound1.setWidth(BigInteger.ONE);
		bound1.setX(BigInteger.ONE);
		bound1.setY(BigInteger.ONE);
		shape1.setBounds(bound1);
		DiagramShapeType shape2 = new DiagramShapeType();
		DiagramBoundsType bound2 = new DiagramBoundsType();
		bound2.setHeight(BigInteger.ONE);
		bound2.setWidth(BigInteger.ONE);
		bound2.setX(BigInteger.ONE);
		bound2.setY(BigInteger.ONE);
		shape2.setBounds(bound2);
		shape2.setElement("0xb00001");
		edge.getPoint().add(point);
		edge.getPoint().add(point2);
		plane.getEdge().add(edge);
		plane.getShape().add(shape1);
		plane.getShape().add(shape2);
		diaType.setPlane(plane);
		
		NetworkType netType = new NetworkType();
		netType.setId("myNetwork");
		NetworkProducerType producer1 = new NetworkProducerType();
		producer1.setId("0xa00001");
		producer1.setName("producer-1");
		netType.getProducer().add(producer1);
		NetworkConsumerType consumer1 = new NetworkConsumerType();
		consumer1.setId("0xb00001");
		consumer1.setName("consumer-1");
		NetworkConnectorType connector1 = new NetworkConnectorType();
		connector1.setId("0xc00001");
		connector1.setName("connector-1");
		connector1.setSourceRef("0xa00001");
		connector1.setTargetRef("0xb00001");
		NetworkExpressionType expression = new NetworkExpressionType();
		expression.setLanguage("java");
		expression.setContent("System.out.println(\"hello\");");
		connector1.setExpression(expression);
		netType.getConsumer().add(consumer1);
		netType.getConnector().add(connector1);
		defType.setDiagram(diaType);
		defType.setNetwork(netType);
		try {
			JAXBContext ctx = JAXBContext.newInstance("com.plugtree.bi.jaxb");
			Marshaller m = ctx.createMarshaller();
			JAXBElement<DefinitionsType> obj = new ObjectFactory().createDefinitions(defType);
			StringWriter writer = new StringWriter();
			m.marshal(obj, writer);
			System.out.println(writer.toString());
		} catch (JAXBException e) {
			
		}
	}
}
