package technology.dubaileading.maccessemployee.rest.entity

import com.google.gson.annotations.SerializedName

data class Profile(

	@field:SerializedName("statuscode")
	val statuscode: String? = null,

	@field:SerializedName("data")
	val profileData: ProfileData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CompanyLocation(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Organisation(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ProfileData(

	@field:SerializedName("country")
	val country: Country? = null,

	@field:SerializedName("shift")
	val shift: Shift? = null,

	@field:SerializedName("organisation")
	val organisation: Organisation? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("organisation_id")
	val organisationId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

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

	@field:SerializedName("grade")
	val grade: Grade? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("timezone_id")
	val timezoneId: Int? = null,

	@field:SerializedName("date_of_joining")
	val dateOfJoining: String? = null,

	@field:SerializedName("old_id")
	val oldId: Any? = null,

	@field:SerializedName("date_of_leaving")
	val dateOfLeaving: Any? = null,

	@field:SerializedName("designation")
	val designation: Designation? = null,

	@field:SerializedName("mobile_login_allowed")
	val mobileLoginAllowed: String? = null,

	@field:SerializedName("qr_image")
	val qrImage: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("nationality_id")
	val nationalityId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("company_email")
	val companyEmail: String? = null,

	@field:SerializedName("access_pin")
	val accessPin: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("manager_id")
	val managerId: Any? = null,

	@field:SerializedName("grade_id")
	val gradeId: Int? = null,

	@field:SerializedName("branch_id")
	val branchId: Int? = null,

	@field:SerializedName("ot_allowed")
	val otAllowed: String? = null,

	@field:SerializedName("shift_id")
	val shiftId: Int? = null,

	@field:SerializedName("geo_fencing_allowed")
	val geoFencingAllowed: String? = null,

	@field:SerializedName("company")
	val company: Company? = null,

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

	@field:SerializedName("manager")
	val manager: Any? = null,

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

	@field:SerializedName("current_shift_id")
	val currentShiftId: Int? = null,

	@field:SerializedName("company_location")
	val companyLocation: CompanyLocation? = null,

	@field:SerializedName("category")
	val category: Category? = null
)

data class Category(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Country(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Grade(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
