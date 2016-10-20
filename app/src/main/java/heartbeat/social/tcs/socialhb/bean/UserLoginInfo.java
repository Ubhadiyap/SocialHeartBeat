package heartbeat.social.tcs.socialhb.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserLoginInfo {

    @SerializedName("emp_id")
    @Expose
    private String empId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * @return The empId
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param empId The emp_id
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

}
