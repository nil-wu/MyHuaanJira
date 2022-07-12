import publish.AutoPublish;
import publish.ClaimAutoPublish;

public class Main2 {

    public static void main(String[] args) throws Exception{

        AutoPublish autoPublish = new ClaimAutoPublish();
        autoPublish.createPublish("REQ-14088");
    }
}
