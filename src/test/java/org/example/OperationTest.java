package org.example;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OperationTest {
    private static ArrayList<Operation> operations = new ArrayList<>();
    private static int sumDbt, sumCdt, balance, startRest, balanceRestXml;

    @Test
    public void checkOperation() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        parser.parse(new File("C:\\Idea project\\headHunterTest\\Balance.xml"), handler);
    }

    public class XMLHandler extends DefaultHandler {
        private String lastElementName;
        private String data, corAcc, dbt, cdt;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if (qName.equals("Ballance")) {
                balanceRestXml = Integer.parseInt(attributes.getValue("Rest"));

            }

            if (qName.equals("Oper")) {
                data = attributes.getValue("data");
                corAcc = attributes.getValue("corAcc");
                dbt = attributes.getValue("dbt");
                cdt = attributes.getValue("cdt");

                Assert.assertNotNull(corAcc); //5. Узел corAcc должен быть атрибутом corAcc узла Operation для расходных операций (с заполненым атрибутом cdt).
//                 использовать при тестировании "без учёта статуса".
//                if (cdt == null) {
//                    operations.add(new Operation(data, corAcc, dbt));
//                    sumDbt += Integer.parseInt(dbt);
//                } else if (dbt == null) {//6. У узла Oper должен быть либо атрибут cdt, либо dbt. Ожидаемый результат dbt null факт ""
//                    operations.add(new Operation(data, corAcc, cdt));
//                    sumCdt += Integer.parseInt(cdt);
//                }
            }
            lastElementName = qName;

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (lastElementName.equals("Status") && information.equals("Выполнена")) {

                if (cdt == null) {
                    operations.add(new Operation(data, corAcc, dbt));
                    sumDbt += Integer.parseInt(dbt);
                } else if (dbt == null) {//6. У узла Oper должен быть либо атрибут cdt, либо dbt. Ожидаемый результат dbt null факт ""
                    operations.add(new Operation(data, corAcc, cdt));
                    sumCdt += Integer.parseInt(cdt);
                }
            }

            if(lastElementName.equals("StartRest") && !information.isEmpty()){
                startRest = Integer.parseInt(information);
                System.out.println(startRest);
            }
        }
    }

    @Test
    public void checkAmount() {
        balance = startRest + (sumDbt - sumCdt);
        Assert.assertEquals(balance, balanceRestXml); // Остаток + баланс = Balance Rest
        System.out.println(sumCdt + " кредит    " + sumDbt + " дебет");
        System.out.println(balance + " остаток");

    }
}
