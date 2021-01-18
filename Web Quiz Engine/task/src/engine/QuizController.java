package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController {
//    ArrayList<Quiz> quizzes;
    QuizService quizzes;

    @Autowired
    public QuizController(QuizService quizzes) {
        this.quizzes = quizzes;
    }


    public void convertFromQuiz(ModelMap model, Quiz quiz) {
        model.addAttribute("id", quiz.getId());
        model.addAttribute("title", quiz.getTitle());
        model.addAttribute("text", quiz.getText());
        model.addAttribute("options", quiz.getOptions());
    }

    @GetMapping(value = "/api/quizzes/{id}", produces = "application/json")
    public ResponseEntity getApiQuiz(@PathVariable long id, ModelMap model) {
//        if (id > quizzes.size() || id < 0) {
//            return ResponseEntity.notFound().build();
//        }
//        Quiz quiz = quizzes.get((int) id);
//        convertFromQuiz(model, quiz);
//        return ResponseEntity.ok(model);
        try {
            Quiz quiz = quizzes.findById(id);
            convertFromQuiz(model, quiz);
            return ResponseEntity.ok(quiz);

        } catch (IllegalArgumentException e) {
            System.out.println("GET api/quizzes/{id} - id does not exist");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/api/quizzes", produces = "application/json")
    public ResponseEntity<ArrayList<ModelMap>> getAllQuizzes() {
        ArrayList<ModelMap> models = new ArrayList<>();
        quizzes.forEach(x -> {
            ModelMap model = new ModelMap();
            convertFromQuiz(model, x);
            models.add(model);
        });
        return ResponseEntity.ok(models);

    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> postNewQuiz(@RequestBody ModelMap model) throws JsonProcessingException {
        System.out.println(model);
        String name = (String) model.getOrDefault("text", "");
        String title = (String) model.getOrDefault("title", "");
        List options = (List) model.getOrDefault("options", new ArrayList<>());
//        System.out.println("name " + name + " title " + title);
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(title) || options.size() < 2) {
            return ResponseEntity.badRequest().build();
        }
        ObjectMapper mapper = new ObjectMapper();

        Quiz quiz = convertToQuiz(model);
        quiz.setId((long) quizzes.size());
//        System.out.println(quiz);
        quizzes.add(quiz);
        return ResponseEntity.ok(mapper.writeValueAsString(quiz));
    }

    private Quiz convertToQuiz(ModelMap model) {
        Quiz q = new Quiz();
        List options;
        q.setTitle((String) model.get("title"));
        q.setText((String) model.get("text"));
        options = (List) model.get("options");
        List answers;
        if (model.containsAttribute("answer") )
            answers = (List) model.getAttribute("answer");
        else
            answers = new ArrayList<>();

        if (options == null) {
            options = new ArrayList<>();
        }

//        q.setAnswer((int) model.get("answer"))
        List<String> arr = new ArrayList<>();
        options.forEach(s -> arr.add((String) s));
        q.setOptions(arr);

        List<Integer> res = new ArrayList<>();
        answers.forEach(a -> res.add((int) a));
        q.setAnswer(answers);
        return q;
    }


    @PostMapping(value = "/api/quizzes/{id}/solve",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ModelMap> postQuizAnswer(
//            @RequestParam("answer") Object answer,
            @PathVariable() int id,
            @RequestBody ModelMap model) {
        if (id > quizzes.size() || id < 0) {
//            System.out.println(model);
            return ResponseEntity.notFound().build();
        }
//        System.out.println(model);
        Quiz quiz = quizzes.get(id);
//        System.out.println(quiz);
        List<Integer> answers = new ArrayList<>();
        ((List) model.getAttribute("answer")).forEach(a -> answers.add((int) a));

        System.out.println(answers);
        boolean success = true;
        if (answers.size() == quiz.getAnswer().size() ) {
            for (Integer ans : answers) {
               int index = quiz.getAnswer().indexOf(ans);
               if (index == -1) {
                   success =false;
                   break;
               }
            }
        } else {
            success = false;
        }
        model.remove("answer");
        String feedback = success ? "Congratulations, you're right!"
                : "Wrong answer! Please, try again.";
        model.addAttribute("success", success);
        model.addAttribute("feedback", feedback);
        return ResponseEntity.ok(model);
    }
}
