package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.MessageDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

import static okhttp.LoginTestsOkhttp.JSON;

public class CreateContactOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29uaWNib29tQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzQ0MTg2MjY3LCJpYXQiOjE3NDM1ODYyNjd9.kcf_--M7p-FokUYw9pKHOMtrKG6HQAFwMZ4VawaOVi0";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String id;

    @Test
    public void createContactSuccess() throws IOException {
        int i = new Random().nextInt(1000);
        ContactDto contactDto= ContactDto.builder()
                .name("Strong"+i)
                .lastName("Mark")
                .email("Sm@gmail.com")
                .phone("123456789"+i)
                .address("Haifa")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(messageDto.getMessage());
        String[] messageDtoM=messageDto.getMessage().toString().split(": ");

        id=messageDtoM[1];
        System.out.println("id="+id);

    }
}
