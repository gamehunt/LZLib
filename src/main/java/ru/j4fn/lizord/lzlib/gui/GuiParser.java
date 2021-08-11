package ru.j4fn.lizord.lzlib.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class GuiParser {

    private static SAXParserFactory factory = SAXParserFactory.newInstance();
    private static SAXParser        parser;

    public static GuiElementWrapper parse(ResourceLocation loc) {
        try {
            if (parser == null) {
                parser = factory.newSAXParser();
            }
            XmlHandler handler = new XmlHandler();
            parser.parse(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream(), handler);
            return handler.getResult();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static class XmlHandler extends DefaultHandler{
        private GuiElementWrapper root;
        public GuiElementWrapper getResult(){
            return root;
        }
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            int width  = Integer.parseInt(attributes.getValue("width"));
            int height = Integer.parseInt(attributes.getValue("height"));
            int x  = Integer.parseInt(attributes.getValue("x"));
            int y  = Integer.parseInt(attributes.getValue("y"));
            if (qName.equals("gui")) {
                if(root != null){
                    this.fatalError(new SAXParseException("Root element declared twice", null));
                }
                root = new GuiElementWrapper(null);
                root.setSize(width, height);
                root.move(x, y);
            }else if(qName.equals("button")){
                Class<? extends AbstractButton> clazz = null;
                String className = attributes.getValue("class");
                try {
                    clazz = (Class<? extends AbstractButton>) Class.forName(className);
                } catch (Exception e) {
                    this.fatalError(new SAXParseException("Class not found or not valid: "+className, null));
                }
                GuiElementWrapper wrap = null;
                try {
                    wrap = new GuiElementWrapper(clazz.newInstance());
                    wrap.setSize(width, height);
                    wrap.move(x, y);
                } catch (Exception e) {
                    this.fatalError(new SAXParseException("Class default constructor not found: "+className, null));
                }
                if(root != null) {
                    root.addChild(wrap);
                }else{
                    this.fatalError(new SAXParseException("GUI root not defined", null));
                }
            }else if(qName.equals("text")){
                Class<? extends AbstractGuiTextBlock> clazz = null;
                String className = attributes.getValue("class");
                try {
                    clazz = (Class<? extends AbstractGuiTextBlock>) Class.forName(className);
                } catch (Exception e) {
                    this.fatalError(new SAXParseException("Class not found or not valid: "+className, null));
                }
                GuiElementWrapper wrap = null;
                try {
                    AbstractGuiTextBlock bl = clazz.newInstance();
                    bl.setText(attributes.getValue("value"));
                    bl.setColor(Integer.parseInt(attributes.getValue("color")));
                    wrap = new GuiElementWrapper(bl);
                    wrap.setSize(width, height);
                    wrap.move(x, y);
                } catch (IllegalAccessException | InstantiationException e) {
                    this.fatalError(new SAXParseException("Class default constructor not found: "+className, null));
                } catch (NullPointerException e){
                    this.fatalError(new SAXParseException("Invalid parameters", null));
                }
                if(root != null) {
                    root.addChild(wrap);
                }else{
                    this.fatalError(new SAXParseException("GUI root not defined", null));
                }
            }
        }
    }
}
