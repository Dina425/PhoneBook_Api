package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkhttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();//send btn

    @Test
    public void registrationSuccess() throws IOException {
        int i = new Random().nextInt(1000);
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("sonicboom"+i+"@gmail.com")
                .password("Snow123456!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDto responseDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }
    @Test
    public void registrationWrongEmail() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("sonicboomgmail.com")
                .password("Snow123456!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertTrue(errorDto.getMessage().toString().contains("username=must be a well-formed email address"));
        Assert.assertEquals(errorDto.getPath(),"/v1/user/registration/usernamepassword");


    }
    @Test
    public void registrationWrongPAssword() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("sonicboom1223@gmail.com")
                .password("Snow123456")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertTrue(errorDto.getMessage().toString().contains("password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]"));

        Assert.assertEquals(errorDto.getPath(),"/v1/user/registration/usernamepassword");


    }
    @Test
    public void registrationDuplicateUser() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("sonicboom@gmail.com")
                .password("Snow123456!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 409);
        ErrorDto errorDto = gson.fromJson(response.body().string(),dto.ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),409);
        Assert.assertEquals(errorDto.getMessage(),"User already exists");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/registration/usernamepassword");


    }


}
