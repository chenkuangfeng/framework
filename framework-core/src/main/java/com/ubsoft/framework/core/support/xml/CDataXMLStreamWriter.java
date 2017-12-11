package com.ubsoft.framework.core.support.xml;

import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class CDataXMLStreamWriter extends DelegatingXMLStreamWriter
{
   private static final Pattern XML_CHARS = Pattern.compile( "[&<>]" );
 
   public CDataXMLStreamWriter( XMLStreamWriter del )
   {
      super( del );
   }
 
   @Override
   public void writeCharacters( String text ) throws XMLStreamException
   {
      boolean useCData = XML_CHARS.matcher( text ).find();
      if( useCData )
      {
         super.writeCData( text );
      }
      else
      {
         super.writeCharacters( text );
      }
   }
}