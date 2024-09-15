package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Component
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;
    private final Gson gson = new Gson();

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        response.getWriter().print(gson.toJson(data));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        try {
            final var post = service.getById(id);
            response.getWriter().print(gson.toJson(post));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print(gson.toJson(e.getMessage()));
        }
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        try {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print(gson.toJson(e.getMessage()));
        }
    }
}