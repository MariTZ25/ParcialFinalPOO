/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package packagee;

/**
 *
 * @author Sahid
 */
public class Response {
    private int code;
    private String message;
    private String data;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }
}
