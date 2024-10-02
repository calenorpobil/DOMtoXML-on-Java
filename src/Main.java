import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class Main {
	public static void main(String args[]) throws IOException, ClassNotFoundException {
		Persona persona;
		File fichero = new File("FichPersona2.dat");

		ObjectInputStream dataIS = new ObjectInputStream(new FileInputStream(fichero));
		int id, dep, posicion = 0;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		int i = 1;
		Document document = null;
		try 
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();

			document = implementation.createDocument(null,  "Personas", null);
			document.setXmlVersion("1.0");

			try 
			{
				while (true) 
				{
					persona = (Persona) dataIS.readObject();
					System.out.println(String.format("Nombre: %s, edad: %d", persona.getNombre(), persona.getEdad()));
					Element raiz = document.createElement("Persona"); //nodo empleado

					document.getDocumentElement().appendChild(raiz);

					crearElemento("nombre", persona.getNombre(), raiz, document);
					crearElemento("edad", persona.getEdad()+"", raiz, document);
				}
			} catch (IOException ex)
			{
				System.out.println("FIN DE LECTURA");
			}

		} catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataIS.close();
		try
		{
			Source source = new DOMSource(document);
			Result result = new StreamResult(new java.io.File("MisPersonas.xml"));
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		}catch (TransformerException ex) {
			
		}

	}
	static void  crearElemento(String datoEmple, String valor, Element raiz, Document document){ 
		Element elem = document.createElement(datoEmple); // crea el nodo hijo
		Text text = document.createTextNode(valor); // damos valor
		raiz.appendChild(elem); //pegamos el elemento hijo a la raiz
		elem.appendChild(text); //pegamos el valor		 	
	} // fin m�todo CrearElemnto //
}
