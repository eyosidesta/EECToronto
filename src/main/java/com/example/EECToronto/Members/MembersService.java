package com.example.EECToronto.Members;

import com.example.EECToronto.TeamMembers.TeamMembersRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class MembersService {
    private final MembersRepository membersRepository;
    private final TeamMembersRepository teamMembersRepository;
    
    @Autowired
    public MembersService(MembersRepository membersRepository, TeamMembersRepository teamMembersRepository) {
        this.membersRepository = membersRepository;
        this.teamMembersRepository = teamMembersRepository;
    }
    public List<Members> getAllMembersService() {
        return membersRepository.findAll();
    }
    public void addMemberService(Members members) {
        Optional<Members> findMemberByEmail = membersRepository.findMemberByEmail(members.getEmail());
        if (findMemberByEmail.isPresent()) {
            throw new IllegalStateException("Email Taken");
        }
        Optional<Members> findMemberByPhone = membersRepository.findMemberByPhone(members.getPhone());
        if (findMemberByPhone.isPresent()) {
            throw new IllegalStateException("Phone Taken");
        }
        membersRepository.save(members);
    }

    public Members updateMemberService(Long id, Members updatedMember) {
        Members existingMember = membersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Check if email is being changed and if new email is already taken
        if (!existingMember.getEmail().equals(updatedMember.getEmail())) {
            Optional<Members> findMemberByEmail = membersRepository.findMemberByEmail(updatedMember.getEmail());
            if (findMemberByEmail.isPresent() && !findMemberByEmail.get().getId().equals(id)) {
                throw new IllegalStateException("Email Taken");
            }
        }

        // Check if phone is being changed and if new phone is already taken
        if (!existingMember.getPhone().equals(updatedMember.getPhone())) {
            Optional<Members> findMemberByPhone = membersRepository.findMemberByPhone(updatedMember.getPhone());
            if (findMemberByPhone.isPresent() && !findMemberByPhone.get().getId().equals(id)) {
                throw new IllegalStateException("Phone Taken");
            }
        }

        // Update fields
        existingMember.setName(updatedMember.getName());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setPhone(updatedMember.getPhone());
        existingMember.setGender(updatedMember.getGender());
        if (updatedMember.getDate_of_birth() != null) {
            existingMember.setDate_of_birth(updatedMember.getDate_of_birth());
        }
        if (updatedMember.getAddress() != null) {
            existingMember.setAddress(updatedMember.getAddress());
        }

        return membersRepository.save(existingMember);
    }

    public Members getMemberById(Long id) {
        return membersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public void deleteMemberService(Long id) {
        Members member = membersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        // Delete all TeamMembers records associated with this member
        teamMembersRepository.findTeamsByMembers(member).forEach(teamMembersRepository::delete);
        
        // Delete the member
        membersRepository.delete(member);
    }

    public ImportResult importMembersFromExcel(MultipartFile file) {
        ImportResult result = new ImportResult();
        
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("File name is empty");
            }

            InputStream inputStream = file.getInputStream();
            Workbook workbook;
            
            // Determine file type and create appropriate workbook
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".csv")) {
                // For CSV, we'll use a simple text-based parser
                return importMembersFromCSV(inputStream);
            } else {
                throw new RuntimeException("Unsupported file format. Please use .xls, .xlsx, or .csv");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            // Process data rows
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    processMemberRow(row, result);
                } catch (Exception e) {
                    result.addError("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }

        return result;
    }

    private void processMemberRow(Row row, ImportResult result) {
        // Get cell values (Name, Phone, Gender, Email, DateOfBirth, Address)
        // Required: Name, Phone, Gender
        // Optional: Email, DateOfBirth, Address
        String name = getCellValueAsString(row.getCell(0));
        String phone = getCellValueAsString(row.getCell(1));
        String gender = getCellValueAsString(row.getCell(2));
        String email = getCellValueAsString(row.getCell(3));
        String dateOfBirthStr = getCellValueAsString(row.getCell(4));
        String address = getCellValueAsString(row.getCell(5));

        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("Phone is required");
        }
        if (gender == null || gender.trim().isEmpty()) {
            throw new RuntimeException("Gender is required");
        }

        // Check if member already exists
        Optional<Members> existingByPhone = membersRepository.findMemberByPhone(phone.trim());
        Optional<Members> existingByEmail = Optional.empty();
        
        if (email != null && !email.trim().isEmpty()) {
            existingByEmail = membersRepository.findMemberByEmail(email.trim());
        }

        if (existingByPhone.isPresent() || existingByEmail.isPresent()) {
            result.incrementSkipped();
            return;
        }

        // Create new member
        Members member = new Members();
        member.setName(name.trim());
        member.setPhone(phone.trim());
        member.setGender(gender.trim()); // Gender is required
        
        if (email != null && !email.trim().isEmpty()) {
            member.setEmail(email.trim());
        }
        
        if (dateOfBirthStr != null && !dateOfBirthStr.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                member.setDate_of_birth(new java.sql.Date(sdf.parse(dateOfBirthStr.trim()).getTime()));
            } catch (Exception e) {
                // If date parsing fails, skip it
            }
        }
        
        if (address != null && !address.trim().isEmpty()) {
            member.setAddress(address.trim());
        }

        try {
            membersRepository.save(member);
            result.incrementSuccess();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save member: " + e.getMessage());
        }
    }

    private ImportResult importMembersFromCSV(InputStream inputStream) {
        ImportResult result = new ImportResult();
        
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                if (values.length < 3) {
                    result.addError("Invalid CSV row: " + line);
                    continue;
                }

                try {
                    // Column order: Name, Phone, Gender, Email, DateOfBirth, Address
                    // Required: Name, Phone, Gender
                    // Optional: Email, DateOfBirth, Address
                    String name = values.length > 0 ? values[0].trim() : "";
                    String phone = values.length > 1 ? values[1].trim() : "";
                    String gender = values.length > 2 ? values[2].trim() : "";
                    String email = values.length > 3 ? values[3].trim() : "";
                    String dateOfBirthStr = values.length > 4 ? values[4].trim() : "";
                    String address = values.length > 5 ? values[5].trim() : "";

                    if (name.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
                        result.addError("Row missing required fields (Name, Phone, Gender)");
                        continue;
                    }

                    // Check if member exists
                    Optional<Members> existingByPhone = membersRepository.findMemberByPhone(phone);
                    Optional<Members> existingByEmail = Optional.empty();
                    
                    if (!email.isEmpty()) {
                        existingByEmail = membersRepository.findMemberByEmail(email);
                    }

                    if (existingByPhone.isPresent() || existingByEmail.isPresent()) {
                        result.incrementSkipped();
                        continue;
                    }

                    // Create new member
                    Members member = new Members();
                    member.setName(name);
                    member.setPhone(phone);
                    member.setGender(gender); // Gender is required
                    if (!email.isEmpty()) {
                        member.setEmail(email);
                    }
                    if (!dateOfBirthStr.isEmpty()) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            member.setDate_of_birth(new java.sql.Date(sdf.parse(dateOfBirthStr).getTime()));
                        } catch (Exception e) {
                            // Skip date if parsing fails
                        }
                    }
                    if (!address.isEmpty()) {
                        member.setAddress(address);
                    }

                    membersRepository.save(member);
                    result.incrementSuccess();
                } catch (Exception e) {
                    result.addError("Error processing row: " + e.getMessage());
                }
            }

            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file: " + e.getMessage(), e);
        }

        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Convert numeric to string without decimal if it's a whole number
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    // Inner class for import results
    public static class ImportResult {
        private int successCount = 0;
        private int skippedCount = 0;
        private List<String> errors = new ArrayList<>();

        public void incrementSuccess() {
            successCount++;
        }

        public void incrementSkipped() {
            skippedCount++;
        }

        public void addError(String error) {
            errors.add(error);
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getSkippedCount() {
            return skippedCount;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
