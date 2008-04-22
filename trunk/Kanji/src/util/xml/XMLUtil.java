package util.xml;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XMLUtil {
	
	public static void String2ArrayListOfString(String str, ArrayList<String> als, String sep){
		als.clear();
		String str_t = new String(str);
		while(str_t.contains(sep)){
			if(str_t.indexOf(sep) != -1) als.add(new String(str_t.substring(0, str_t.indexOf(sep))));
			else als.add(new String(str_t));
			str_t = str_t.substring(str_t.indexOf(sep)+1);
		}
	}
	
	public static void generateKanaLessonFromPlainText(String text_in, String filename_out){
		// parse input
		ArrayList<String> als = new ArrayList<String>();
		String sep = " ";
		String2ArrayListOfString(text_in, als, sep);
		int size = als.size() / 2;
		
		/*// check parsing
		for(int i=0; i<als.size(); i++){
			System.out.println(als.get(i));
		}*/
		
		// create document with root element
		Document doc = new Document(new Element("lesson"));
		
		// create an element for each char from text_in and add it to root element
		for(int i=0; i<size; i++){
			// maintain data
			String ch = als.get(2*i);
			String read = als.get(2*i+1);
			
			int ch_codepoint = ch.codePointAt(0);
			
			// create blank elements to ease future editing
			Element chchar = new Element("char"); 
			Element reading = new Element("reading");
			Element translation = new Element("translation");
			Element example = new Element("example");
			example.addContent(new Element("text")).addContent(new Element("reading")).addContent(new Element("translation"));
			
			// fill char with content
			chchar.setAttribute("uid", "u+"+Integer.toHexString(ch_codepoint));
			chchar.setAttribute("ch", String.valueOf(ch));
			
			reading.setText(read);
			
			chchar.addContent(reading);
			chchar.addContent(translation);
			chchar.addContent(example);
			
			// add char to lesson
			doc.getRootElement().addContent(chchar);
		}
		
		// write document to file
		try {
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(filename_out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void generateKanjiLessonFromPlainText(String text_in, String filename_out){
		// create document with root element
		Document doc = new Document(new Element("lesson"));
		
		// create an element for each char from text_in and add it to root element
		for(int i=0; i<text_in.length(); i++){
			// maintain data
			char ch = text_in.charAt(i);
			int ch_codepoint = text_in.codePointAt(i);
			
			// create blank elements to ease future editing
			Element chchar = new Element("char"); 
			Element reading = new Element("reading");
			Element translation = new Element("translation");
			Element example = new Element("example");
			example.addContent(new Element("text")).addContent(new Element("reading")).addContent(new Element("translation"));
			
			// fill char with content
			chchar.setAttribute("uid", "u+"+Integer.toHexString(ch_codepoint));
			chchar.setAttribute("ch", String.valueOf(ch));
			
			chchar.addContent(reading);
			chchar.addContent(translation);
			chchar.addContent(example);
			
			// add char to lesson
			doc.getRootElement().addContent(chchar);
		}
		
		// write document to file
		try {
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getPrettyFormat());
			out.output(doc, new FileOutputStream(filename_out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void generateKanjiLessonFromXML1(String filename_in, String filename_out) throws JDOMException, IOException{
		// parse input document
		SAXBuilder builder = new SAXBuilder();
		Document d_in = builder.build(new File(filename_in));
		Element d_in_root = d_in.getRootElement();
		
		// create document with root element
		Document d_out = new Document(new Element("lesson"));
		
		// create an element for each <tr> from filename_in and add it to root element
		ArrayList<Element> trs = new ArrayList<Element>(d_in_root.getChildren("tr"));
		for(int i=0; i<trs.size(); i++){			
			// create blank elements to ease future editing
			Element chchar = new Element("char"); 
			Element reading_on = new Element("reading");
			reading_on.setAttribute("type", "on");
			Element reading_kun = new Element("reading");
			reading_kun.setAttribute("type", "kun");
			Element translation = new Element("translation");
			Element example = new Element("example");
			example.addContent(new Element("text")).addContent(new Element("reading")).addContent(new Element("translation"));
			
			// maintain data
			String ch = new String();
			Integer ch_codepoint = new Integer(0);
			for(int j=0; j<trs.get(i).getChildren("td").size(); j++){
				if(((Element) trs.get(i).getChildren("td").get(j)).getAttributeValue("class").equals("kanji")){
					ch = ((Element) trs.get(i).getChildren("td").get(j)).getChildText("a");
					ch_codepoint = ch.codePointAt(0);
				}
				if(((Element) trs.get(i).getChildren("td").get(j)).getAttributeValue("class").equals("onyomi")){
					reading_on.setText(((Element) trs.get(i).getChildren("td").get(j)).getText());
				}
				if(((Element) trs.get(i).getChildren("td").get(j)).getAttributeValue("class").equals("kunyomi")){
					reading_kun.setText(((Element) trs.get(i).getChildren("td").get(j)).getText());
				}
				if(((Element) trs.get(i).getChildren("td").get(j)).getAttributeValue("class").equals("meanings")){
					translation.setText(((Element) trs.get(i).getChildren("td").get(j)).getText());
				}
			}
			
			// fill char with content
			chchar.setAttribute("uid", "u+"+Integer.toHexString(ch_codepoint));
			chchar.setAttribute("ch", String.valueOf(ch));
			
			chchar.addContent(reading_on);
			chchar.addContent(reading_kun);
			chchar.addContent(translation);
			chchar.addContent(example);
			
			// add char to lesson
			d_out.getRootElement().addContent(chchar);
		}
		
		// write document to file
		try {
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getPrettyFormat());
			out.output(d_out, new FileOutputStream(filename_out));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args){
		/*// kanji lesson 1
		String text_in = "一右雨円王音下火花貝学気休玉金九空月犬見五口校左三山四子糸字耳七車手十出女小上森人水正生青石赤先千川早草足村大男竹中虫町天田土二日入年白八百文本名木目夕立力林六";
		String filename_out = "kanji_lesson_1.xml";
		generateKanjiLessonFromPlainText(text_in, filename_out);*/
		
		// hiragana
		/*String text_in = "あ a い i う u え e お o か ka き ki く ku け ke こ ko きゃ kya きゅ kyu きょ kyo さ sa し shi す su せ se そ so しゃ sha しゅ shu しょ sho た ta ち chi つ tsu て te と to ちゃ cha ちゅ chu ちょ cho な na に ni ぬ nu ね ne の no にゃ nya にゅ nyu にょ nyo は ha ひ hi ふ fu へ he ほ ho ひゃ hya ひゅ hyu ひょ hyo ま ma み mi む mu め me も mo みゃ mya みゅ myu みょ myo や ya ゆ yu よ yo ら ra り ri る ru れ re ろ ro りゃ rya りゅ ryu りょ ryo わ wa ゐ wi ゑ we を wo ん n が ga ぎ gi ぐ gu げ ge ご go ぎゃ gya ぎゅ gyu ぎょ gyo ざ za じ ji ず zu ぜ ze ぞ zo じゃ ja じゅ ju じょ jo だ da ぢ (ji) づ (zu) で de ど do ぢゃ (ja) ぢゅ(ju) ぢょ(jo) ば ba び bi ぶ bu べ be ぼ bo びゃ bya びゅ byu びょ byo ぱ pa ぴ pi ぷ pu ぺ pe ぽ po ぴゃ pya ぴゅ pyu ぴょ pyo";
		String filename_out = "hiragana_lesson.xml";
		generateKanaLessonFromPlainText(text_in, filename_out);*/
		
		// katakana
		/*String text_in = "ア a イ i ウ u エ e オ o カ ka キ ki ク ku ケ ke コ ko キャ kya キュ kyu キョ kyo サ sa シ shi ス su セ se ソ so シャ sha シュ shu ショ sho タ ta チ chi ツ tsu テ te ト to チャ cha チュ chu チョ cho ナ na ニ ni ヌ nu ネ ne ノ no ニャ nya ニュ nyu ニョ nyo ハ ha ヒ hi フ fu ヘ he ホ ho ヒャ hya ヒュ hyu ヒョ hyo マ ma ミ mi ム mu メ me モ mo ミャ mya ミュ myu ミョ myo ヤ ya ユ yu ヨ yo ラ ra リ ri ル ru レ re ロ ro リャ rya リュ ryu リョ ryo ワ wa ヰ wi ヱ we ヲ wo ン n ガ ga ギ gi グ gu ゲ ge ゴ go ギャ gya ギュ gyu ギョ gyo ザ za ジ ji ズ zu ゼ ze ゾ zo ジャ ja ジュ ju ジョ jo ダ da ヂ (ji) ヅ (zu) デ de ド do ヂャ (ja) ヂュ (ju) ヂョ (jo) バ ba ビ bi ブ bu ベ be ボ bo ビャ bya ビュ byu ビョ byo パ pa ピ pi プ pu ペ pe ポ po ピャ pya ピュ pyu ピョ pyo イェ ye ヴァ va ヴィ vi ヴ vu ヴェ ve ヴォ vo ヴャ vya ヴュ vyu ヴョ vyo シェ she ジェ je チェ che スィ si ズィ zi ティ ti トゥ tu テュ tyu ディ di ドゥ du デュ dyu ツァ tsa ツィ tsi ツェ tse ツォ tso ファ fa フィ fi フェ fe フォ fo フュ fyu ウィ wi ウェ we ウォ wo クァ kwa クィ kwi クェ kwe クォ kwo グァ gwa グィ gwi グェ gwe グォ gwo";
		String filename_out = "katakana_lesson.xml";
		generateKanaLessonFromPlainText(text_in, filename_out);*/
		
		// kanji lesson 1-6
		String filename_in = "kanji2.xml";
		String filename_out = "kanji_lesson_2.xml";
		try {
			generateKanjiLessonFromXML1(filename_in, filename_out);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
