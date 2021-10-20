package org.egov.mr.web.models.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LegacyRow {
	
	@CellAnnotation(index = 0)
	private String   tenantId    ;
	@CellAnnotation(index = 1)
	private String   ulbName    ;
	@CellAnnotation(index = 2)
	private String   registrationNo    ;
	@CellAnnotation(index = 3)
	private String   applicationNo    ;
	@CellAnnotation(index = 4)
	private String   dateOfApplication    ;
	@CellAnnotation(index = 5)
	private String   dateOfRegistration    ;
	@CellAnnotation(index = 6)
	private String   approvedBy    ;
	@CellAnnotation(index = 7)
	private String   placeOfApproval    ;
	@CellAnnotation(index = 8)
	private String   currentStatus    ;
	@CellAnnotation(index = 9)
	private String   marriageWard    ;
	@CellAnnotation(index = 10)
	private String   marriageZone    ;
	@CellAnnotation(index = 11)
	private String   placeOfMarriage    ;
	@CellAnnotation(index = 12)
	private String   brideTitle    ;
	@CellAnnotation(index = 13)
	private String   brideFirstName    ;
	@CellAnnotation(index = 14)
	private String   brideMiddleName    ;
	@CellAnnotation(index = 15)
	private String   brideLastName    ;
	@CellAnnotation(index = 16)
	private String   brideFatherTitle    ;
	@CellAnnotation(index = 17)
	private String   brideFatherFirstName    ;
	@CellAnnotation(index = 18)
	private String   brideFatherMiddleName    ;
	@CellAnnotation(index = 19)
	private String   brideFatherLastName    ;
	@CellAnnotation(index = 20)
	private String   brideMotherTitle    ;
	@CellAnnotation(index = 21)
	private String   brideMotherFirstName    ;
	@CellAnnotation(index = 22)
	private String   brideMotherMiddleName    ;
	@CellAnnotation(index = 23)
	private String   brideMotherLastName    ;
	@CellAnnotation(index = 24)
	private String   brideContact    ;
	@CellAnnotation(index = 25)
	private String   brideEmailAddress    ;
	@CellAnnotation(index = 26)
	private String   isBrideDivyang    ;
	@CellAnnotation(index = 27)
	private String   groomTitle    ;
	@CellAnnotation(index = 28)
	private String   groomFirstName    ;
	@CellAnnotation(index = 29)
	private String   groomMiddleName    ;
	@CellAnnotation(index = 30)
	private String   groomLastName    ;
	@CellAnnotation(index = 31)
	private String   groomFatherTitle    ;
	@CellAnnotation(index = 32)
	private String   groomFatherFirstName    ;
	@CellAnnotation(index = 33)
	private String   groomFatherMiddleName    ;
	@CellAnnotation(index = 34)
	private String   groomFatherLastName    ;
	@CellAnnotation(index = 35)
	private String   groomMotherTitle    ;
	@CellAnnotation(index = 36)
	private String   groomMotherFirstName    ;
	@CellAnnotation(index = 37)
	private String   groomMotherMiddleName    ;
	@CellAnnotation(index = 38)
	private String   groomMotherLastName    ;
	@CellAnnotation(index = 39)
	private String   groomContact    ;
	@CellAnnotation(index = 40)
	private String   groomEmailAddress    ;
	@CellAnnotation(index = 41)
	private String   isGroomDivyang    ;
	@CellAnnotation(index = 42)
	private String   brideAddressLine1    ;
	@CellAnnotation(index = 43)
	private String   brideAddressLine2    ;
	@CellAnnotation(index = 44)
	private String   brideAddressLine3    ;
	@CellAnnotation(index = 45)
	private String   brideCountry    ;
	@CellAnnotation(index = 46)
	private String   brideState    ;
	@CellAnnotation(index = 47)
	private String   brideDistrict    ;
	@CellAnnotation(index = 48)
	private String   bridePincode    ;
	@CellAnnotation(index = 49)
	private String   brideDateOfBirth    ;
	@CellAnnotation(index = 50)
	private String   brideAge    ;
	@CellAnnotation(index = 51)
	private String   brideGuardianRelationship    ;
	@CellAnnotation(index = 52)
	private String   brideGuardianName    ;
	@CellAnnotation(index = 53)
	private String   brideGuardianAddressLine1    ;
	@CellAnnotation(index = 54)
	private String   brideGuardianAddressLine2    ;
	@CellAnnotation(index = 55)
	private String   brideGuardianAddressLine3    ;
	@CellAnnotation(index = 56)
	private String   brideGuardianContact    ;
	@CellAnnotation(index = 57)
	private String   brideGuardianCountry    ;
	@CellAnnotation(index = 58)
	private String   brideGuardianState    ;
	@CellAnnotation(index = 59)
	private String   brideGuardianDistrict    ;
	@CellAnnotation(index = 60)
	private String   brideGuardianEmail    ;
	@CellAnnotation(index = 61)
	private String   brideGuardianPin    ;
	@CellAnnotation(index = 62)
	private String   groomAddressLine1    ;
	@CellAnnotation(index = 63)
	private String   groomAddressLine2    ;
	@CellAnnotation(index = 64)
	private String   groomAddressLine3    ;
	@CellAnnotation(index = 65)
	private String   groomCountry    ;
	@CellAnnotation(index = 66)
	private String   groomState    ;
	@CellAnnotation(index = 67)
	private String   groomDistrict    ;
	@CellAnnotation(index = 68)
	private String   groomPinCode    ;
	@CellAnnotation(index = 69)
	private String   groomDateOfBirth    ;
	@CellAnnotation(index = 70)
	private String   groomAge    ;
	@CellAnnotation(index = 71)
	private String   groomGuardianRelationship    ;
	@CellAnnotation(index = 72)
	private String   groomGuardianName    ;
	@CellAnnotation(index = 73)
	private String   groomGuardianAddressLine1    ;
	@CellAnnotation(index = 74)
	private String   groomGuardianAddressLine2    ;
	@CellAnnotation(index = 75)
	private String   groomGuardianAddressLine3    ;
	@CellAnnotation(index = 76)
	private String   groomGuardianCountry    ;
	@CellAnnotation(index = 77)
	private String   groomGuardianState    ;
	@CellAnnotation(index = 78)
	private String   groomGuardianDistrict    ;
	@CellAnnotation(index = 79)
	private String   groomGuardianEmail    ;
	@CellAnnotation(index = 80)
	private String   groomGuardianPin    ;
	@CellAnnotation(index = 81)
	private String   witness1Title    ;
	@CellAnnotation(index = 82)
	private String   witness1Address    ;
	@CellAnnotation(index = 83)
	private String   witness1Contact    ;
	@CellAnnotation(index = 84)
	private String   witness1Country    ;
	@CellAnnotation(index = 85)
	private String   witness1State    ;
	@CellAnnotation(index = 86)
	private String   witness1District    ;
	@CellAnnotation(index = 87)
	private String   witness1Pin    ;
	@CellAnnotation(index = 88)
	private String   witness1FirstName    ;
	@CellAnnotation(index = 89)
	private String   witness1MiddleName    ;
	@CellAnnotation(index = 90)
	private String   witness1LastName    ;
	@CellAnnotation(index = 91)
	private String   witness2Title    ;
	@CellAnnotation(index = 92)
	private String   witness2Address    ;
	@CellAnnotation(index = 93)
	private String   witness2Contact    ;
	@CellAnnotation(index = 94)
	private String   witness2Country    ;
	@CellAnnotation(index = 95)
	private String   witness2State    ;
	@CellAnnotation(index = 96)
	private String   witness2District    ;
	@CellAnnotation(index = 97)
	private String   witness2Pin    ;
	@CellAnnotation(index = 98)
	private String   witness2FirstName    ;
	@CellAnnotation(index = 99)
	private String   witness2MiddleName    ;
	@CellAnnotation(index = 100)
	private String   witness2LastName    ;

}
