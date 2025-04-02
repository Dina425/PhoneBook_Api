package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ErrorDto;
import dto.MessageDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static okhttp.LoginTestsOkhttp.JSON;

public class DeleteContactByIdOkhttp {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29uaWNib29tQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzQ0MTg2MjY3LCJpYXQiOjE3NDM1ODYyNjd9.kcf_--M7p-FokUYw9pKHOMtrKG6HQAFwMZ4VawaOVi0";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String id;

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact //
        //get id from message:"message": "Contact was added! ID: a4a33d33-b00e-4049-8570-dfd07aace9c7"
        //id = ""
        ContactDto contactDto= ContactDto.builder()
                .name("Strong")
                .lastName("Mark")
                .email("Sm@gmail.com")
                .phone("123456789122")
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
        //System.out.println(messageDto.getMessage());
        String[] messageDtoM=messageDto.getMessage().toString().split(": ");

        id=messageDtoM[1];
        //System.out.println("id="+id);

    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(messageDto.getMessage());
        Assert.assertEquals(messageDto.getMessage(),"Contact was deleted!");
    }

    @Test
    public void deleteContactByIdWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/18bf1a49-d80d-4961-9fc3-792b3b0971c8")
                .delete()
                .addHeader("Authorization", "ghshfdv")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
    }

    @Test
    public void deleteContactByIdNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/18bf1a49")
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        //System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getMessage(),"Contact with id: 18bf1a49 not found in your contacts!");
    }
}
//3dc754e5-69c3-427a-b666-4240ba0b087c
//asdas@gmail.com
//================================
//        0325278f-29db-4b19-839b-4cada7391839
//asdas1@gmail.com
//================================

