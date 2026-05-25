package modelo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import packagee.HospitalData;

public class LoadData {

    public static void loadUsers() throws Exception {
        HospitalData.users.clear();
        
        String content = new String(Files.readAllBytes(Paths.get("json/users.json")));
        JSONObject root = new JSONObject(content);

        JSONArray users = root.getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = users.getJSONObject(i);
            String type = obj.getString("type");

            if (type.equals("admin")) {
                Administrator admin = new Administrator(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password")
                );
                HospitalData.users.add(admin);
                
            } else if (type.equals("patient")) {
                Patient patient = new Patient(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password"),
                        obj.getString("email"),
                        LocalDate.parse(obj.getString("birthdate")),
                        obj.getBoolean("gender"),
                        obj.getLong("phone"),
                        obj.getString("address")
                );
                HospitalData.users.add(patient);
                
            } else if (type.equals("doctor")) {
                Doctor doctor = new Doctor(
                        obj.getLong("id"),
                        obj.getString("username"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        obj.getString("password"),
                        Specialty.valueOf(obj.getString("specialty")),
                        obj.getString("licenceNumber"),
                        obj.getString("assignedOffice")
                );
                HospitalData.users.add(doctor);
            }
        }
    }
}
