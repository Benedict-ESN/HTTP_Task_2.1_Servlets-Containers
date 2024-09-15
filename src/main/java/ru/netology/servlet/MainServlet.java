package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String API_POSTS = "/api/posts";
  private static final String API_POSTS_ID_PATTERN = "/api/posts/\\d+";

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals("GET")) {
        handleGet(path, resp);
      } else if (method.equals("POST") && path.equals(API_POSTS)) {
        controller.save(req.getReader(), resp);
      } else if (method.equals("DELETE") && path.matches(API_POSTS_ID_PATTERN)) {
        final var id = getIdFromPath(path);
        controller.removeById(id, resp);
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void handleGet(String path, HttpServletResponse resp) throws Exception {
    if (path.equals(API_POSTS)) {
      controller.all(resp);
    } else if (path.matches(API_POSTS_ID_PATTERN)) {
      final var id = getIdFromPath(path);
      controller.getById(id, resp);
    } else {
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private long getIdFromPath(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
  }
}