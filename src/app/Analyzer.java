package app;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    // 🔹 Detect role and map to required skills
    private String getSkillsForRole(String role) {

        role = role.toLowerCase();

        if (role.contains("developer") || role.contains("engineer")) {
            return "java python sql git";
        }
        else if (role.contains("data") || role.contains("analyst")) {
            return "python sql excel statistics";
        }
        else if (role.contains("tester") || role.contains("qa") || role.contains("testing")) {
            return "selenium java testing junit";
        }

        return role; // if user enters skills directly
    }

    // 🔹 Normalize short forms
    private String normalizeSkill(String skill) {
        skill = skill.toLowerCase();

        if (skill.equals("py")) return "python";
        if (skill.equals("js")) return "javascript";
        if (skill.equals("c++")) return "cpp";
        if (skill.equals("c#")) return "csharp";

        return skill;
    }

    // 🔹 Main analysis method
    public Result analyze(String resumeText, String jobInput) {

        // Step 1: convert role → skills
        String jobSkills = getSkillsForRole(jobInput);

        // Step 2: clean resume text
        String resume = " " + resumeText.toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ") + " ";

        // Step 3: normalize words
        resume = resume.replace("c++", "cpp")
                .replace("c#", "csharp")
                .replace("py", "python");

        // Step 4: split skills
        String[] skills = jobSkills.split("\\s+");

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        // Step 5: match logic
        for (String skill : skills) {

            skill = normalizeSkill(skill.trim());
            if (skill.isEmpty()) continue;

            if (resume.matches(".*\\b" + skill + "\\b.*")) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        // Step 6: percentage
        int total = skills.length;
        double percentage = total == 0 ? 0 : (matched.size() * 100.0) / total;

        // Step 7: decision
        String decision;

        if (percentage >= 75) {
            decision = "Can Apply";
        } else if (percentage >= 50) {
            decision = "May Be Apply";
        } else {
            decision = "Can Not Apply";
        }

        return new Result(matched, missing, percentage, decision);
    }
}