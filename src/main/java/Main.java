//IMPORTS
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        //JSOUP DEPENDENCY (VIEW: build.gradle)
        //MORE INFO ON JSOUP: https://jsoup.org/

        //GET URL INPUT FROM USER
        System.out.println("Please enter a valid URL: ");
        String url = new Scanner(System.in).nextLine();

        //Execute the request as a GET, and parse the result.
        Document doc = Jsoup.connect(url).get();

        /*Offline testing string and doc (Need to add comment on line 22)
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p><p>Hello</p>s</body></html>";
        Document doc = Jsoup.parse(html);
        */

        //COUNT LINES
        System.out.println("THE DOCUMENT HAS:\n " + countLines(url) + " LINES");
        //PARAGRAPH COUNT
        System.out.println(" " + countParagraphs(doc) + " <p> ELEMENTS");
        //IMGs COUNT
        System.out.println(" " + countImgs(doc) + " IMAGE(S) INSIDE <p> ELEMENTS.");
        //FORMS COUNT
        System.out.println(" " + countForms(doc) + " FORM(S)");
        //method="GET" FORMS COUNT
        System.out.println("  " + getForms(doc) + " OF METHOD GET");
        //methos="POST" FORMS COUNT
        System.out.println("  " + postForms(doc) + " OF METHOD POST");
        //<INPUTS> OF FORMS
        listType(doc);
        //IF THERE ARE POSTS FORMS, SEND REQUEST
        System.out.println(" " + "SENDING POST REQ. . . RESPONSE:\n\n" +sendPostReq(doc));
    }

    private static int countLines(String URL) throws IOException {
        //EXECUTE GET METHOD AND BRING BACK RAW ANSWER (NO PARSE)
        Connection.Response resp = Jsoup.connect(URL).execute();

        //RETURN THE LENGHT OF THE ARRAY CREATED BY SPLITTING BY \N (NEW LINE)
        return resp.body().split("\n").length;
    }

    private static int countParagraphs(Document DOC) {
        Elements p = DOC.getElementsByTag("p");
        return p.size();
    }

    private static int countImgs(Document DOC) {
        Elements paragraphs = DOC.getElementsByTag("p");
        //Keep track of imgs in all <p> elements
        int imgs = 0;

        for (Element p: paragraphs) {
            //add the amount of images found in that especific <p>
            imgs += p.getElementsByTag("img").size();
        }
        return imgs;
    }

    private static int countForms(Document DOC) {
        Elements forms = DOC.getElementsByTag("form");
        return forms.size();
    }

    private static int getForms(Document DOC) {
        //array list of forms in doc
        Elements forms = DOC.getElementsByTag("form");

        int cantGet = 0;

        for (Element form : forms) {
            if (form.attr("method").equalsIgnoreCase("GET") || form.attr("method").equals("")) {
                cantGet++;
            }
        }
        return cantGet;
    }

    private static int postForms(Document DOC) {
        //array list of forms in doc
        Elements forms = DOC.getElementsByTag("form");

        int cantPost = 0;

        for (Element form : forms) {
            if (form.attr("method").equalsIgnoreCase("POST")) {
                cantPost++;
            }
        }
        return cantPost;
    }

    private static void listType(Document DOC) {
        //array list of forms in doc
        Elements forms = DOC.getElementsByTag("form");

        int formNo = 0; //keep track of forms
        int inputNo; //keep track of inputs in form

        for (Element form : forms) {
            formNo++; //next form
            inputNo = 1; //reset inputs count in form

            //get inputs in form
            Elements inputs = form.getElementsByTag("input");
            //System.out.println(form);
            System.out.println("  " + "IN FORM NO. " + formNo + " THERE ARE " + inputs.size() + " INPUTS.");
            for (Element input : inputs) {
                System.out.println("   INPUT NO." + inputNo + ": " + input.attr("name").toUpperCase() + " OF TYPE " + input.attr("type").toString().toUpperCase());
                inputNo++;
            }
        }
    }

    private static String sendPostReq(Document DOC) throws IOException {
        Elements forms = DOC.getElementsByTag("form");

        String respose = "";

        for (Element form : forms) {
            if (form.attr("method").equalsIgnoreCase("POST")) {
                respose = ((FormElement) form).submit()
                                    .data("asignatura","practica1")
                                    .header("matricula","20160277")
                                    .execute().body();
            }
        }

        if(respose==""){
            return "THERE ARE NO POSTS FORMS.";
        }else{
            return respose;
        }

    }



}
