package com.carocart.authentication.dto;

public class OtpVerifyRequest {
    private String emailOrPhone;
    private String otp;
	public String getEmailOrPhone() {
		return emailOrPhone;
	}
	public void setEmailOrPhone(String emailOrPhone) {
		this.emailOrPhone = emailOrPhone;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}

    
}
