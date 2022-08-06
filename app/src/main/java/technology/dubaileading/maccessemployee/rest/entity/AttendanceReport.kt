package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class AttendenceReport(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val attendanceData: AttendanceData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AttendanceData(

	@field:SerializedName("first_page_url")
	val firstPageUrl: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("last_page")
	val lastPage: Int? = null,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String? = null,

	@field:SerializedName("next_page_url")
	val nextPageUrl: Any? = null,

	@field:SerializedName("from")
	val from: Int? = null,

	@field:SerializedName("to")
	val to: Int? = null,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: Any? = null,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class Designation(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Shift(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Company(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class EmployeeProfile(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("nationality_id")
	val nationalityId: Int? = null,

	@field:SerializedName("shift")
	val shift: Shift? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("company_email")
	val companyEmail: String? = null,

	@field:SerializedName("access_pin")
	val accessPin: String? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("manager_id")
	val managerId: Int? = null,

	@field:SerializedName("grade_id")
	val gradeId: Int? = null,

	@field:SerializedName("branch_id")
	val branchId: Any? = null,

	@field:SerializedName("ot_allowed")
	val otAllowed: String? = null,

	@field:SerializedName("shift_id")
	val shiftId: Int? = null,

	@field:SerializedName("geo_fencing_allowed")
	val geoFencingAllowed: Any? = null,

	@field:SerializedName("company")
	val company: Company? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("department")
	val department: Department? = null,

	@field:SerializedName("email")
	val email: Any? = null,

	@field:SerializedName("emergency_contact_number")
	val emergencyContactNumber: Any? = null,

	@field:SerializedName("address")
	val address: Any? = null,

	@field:SerializedName("company_id")
	val companyId: Int? = null,

	@field:SerializedName("department_id")
	val departmentId: Int? = null,

	@field:SerializedName("designation_id")
	val designationId: Int? = null,

	@field:SerializedName("personal_contact_number")
	val personalContactNumber: Any? = null,

	@field:SerializedName("photo")
	val photo: Any? = null,

	@field:SerializedName("leaving_type")
	val leavingType: Any? = null,

	@field:SerializedName("is_manager")
	val isManager: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("cost_center")
	val costCenter: Int? = null,

	@field:SerializedName("employee_id")
	val employeeId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("timezone_id")
	val timezoneId: Int? = null,

	@field:SerializedName("date_of_joining")
	val dateOfJoining: String? = null,

	@field:SerializedName("current_shift_id")
	val currentShiftId: Int? = null,

	@field:SerializedName("date_of_leaving")
	val dateOfLeaving: Any? = null,

	@field:SerializedName("designation")
	val designation: Designation? = null,

	@field:SerializedName("mobile_login_allowed")
	val mobileLoginAllowed: String? = null,

	@field:SerializedName("qr_image")
	val qrImage: String? = null
)

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("sources")
	val sources: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("shift")
	val shift: Shift? = null,

	@field:SerializedName("face_image")
	val faceImage: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("out_mode")
	val outMode: Any? = null,

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("shift_id")
	val shiftId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("date_time_unix")
	val dateTimeUnix: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("address")
	val address: Any? = null,

	@field:SerializedName("device_id")
	val deviceId: String? = null,

	@field:SerializedName("employee_profile")
	val employeeProfile: EmployeeProfile? = null,

	@field:SerializedName("authdevice")
	val authdevice: Any? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("settings_project_id")
	val settingsProjectId: Any? = null,

	@field:SerializedName("employee_profile_id")
	val employeeProfileId: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("entry_id")
	val entryId: Any? = null,

	@field:SerializedName("device")
	val device: Any? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Department(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
