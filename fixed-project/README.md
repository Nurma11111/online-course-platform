# 🎓 Online Course Platform — Final Project
**Author: Bekzhanuly Nurmukhamed**

A production-ready Spring Boot REST API for an online learning platform.

---

## 🏗 Architecture

```
src/main/java/com/bekzhanuly/courseplatform/
├── config/          # SecurityConfig, SwaggerConfig, AsyncConfig
├── controller/      # REST Controllers (Auth, Course, Lesson, Enrollment, Review, Category, File)
├── dto/
│   ├── request/     # Request DTOs with validation
│   └── response/    # Response DTOs + ApiResponse wrapper
├── entity/          # JPA Entities (6 entities)
├── enums/           # Role, CourseStatus, EnrollmentStatus, LessonType
├── exception/       # Custom exceptions + GlobalExceptionHandler
├── filter/          # Request logging filter
├── mapper/          # MapStruct mappers (6 mappers)
├── repository/      # Spring Data JPA repositories
├── security/
│   ├── jwt/         # JwtUtil, JwtAuthFilter
│   └── service/     # UserDetails, UserDetailsService
└── service/
    ├── async/        # EmailNotificationService, StatisticsService (@Async)
    └── impl/         # AuthService, CourseService, LessonService, EnrollmentService, ReviewService, CategoryService, FileStorageService
```

---

## 📦 Entities (6 total)

| Entity | Description |
|--------|-------------|
| `User` | Students, Instructors, Admins |
| `Course` | Online courses with status, pricing, ratings |
| `Lesson` | Course lessons (video, text, quiz, assignment) |
| `Enrollment` | Student ↔ Course enrollment with progress |
| `Review` | Star ratings + comments for courses |
| `Category` | Course categories |
| `FileAttachment` | Uploaded files attached to lessons |

---

## 🔐 Security

- JWT Authentication (access + refresh tokens)
- BCrypt password encoding (strength 12)
- Role-based authorization: `ROLE_STUDENT`, `ROLE_INSTRUCTOR`, `ROLE_ADMIN`
- `@PreAuthorize` on all protected endpoints
- Stateless sessions (no cookies)

---

## 🌐 API Endpoints

### Auth
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/auth/register` | ❌ | Register new user |
| POST | `/api/auth/login` | ❌ | Login, get JWT |

### Courses (Pagination + Search + Filter + Sort)
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/courses` | ❌ | List courses (paginated, searchable, filterable) |
| GET | `/api/courses/{id}` | ❌ | Get course by ID |
| GET | `/api/courses/top?limit=5` | ❌ | Top courses by enrollment |
| POST | `/api/courses` | INSTRUCTOR/ADMIN | Create course |
| PUT | `/api/courses/{id}` | INSTRUCTOR/ADMIN | Update course |
| PUT | `/api/courses/{id}/publish` | INSTRUCTOR/ADMIN | Publish course |
| DELETE | `/api/courses/{id}` | INSTRUCTOR/ADMIN | Delete course |

**GET /api/courses query params:**
```
search=java        # search in title/description
status=PUBLISHED   # DRAFT / PUBLISHED / ARCHIVED
categoryId=1
instructorId=2
minPrice=0
maxPrice=100
language=English
difficulty=BEGINNER
page=0
size=10
sortBy=rating      # title / price / rating / createdAt / totalStudents
sortDir=desc
```

### Lessons
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/courses/{courseId}/lessons` | ❌ | Get all lessons for course |
| GET | `/api/courses/{courseId}/lessons/{lessonId}` | ❌ | Get lesson by ID |
| POST | `/api/courses/{courseId}/lessons` | INSTRUCTOR/ADMIN | Create lesson |
| PUT | `/api/courses/{courseId}/lessons/{lessonId}` | INSTRUCTOR/ADMIN | Update lesson |
| DELETE | `/api/courses/{courseId}/lessons/{lessonId}` | INSTRUCTOR/ADMIN | Delete lesson |

### Enrollments
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/enrollments/courses/{courseId}` | STUDENT | Enroll in course |
| GET | `/api/enrollments/my?page=0&size=10` | AUTH | My enrollments |
| GET | `/api/enrollments/courses/{courseId}?page=0` | INSTRUCTOR/ADMIN | Course enrollments |
| PUT | `/api/enrollments/{id}/progress?progress=75` | STUDENT | Update progress |
| DELETE | `/api/enrollments/{id}` | STUDENT | Drop course |

### Reviews
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/courses/{courseId}/reviews?page=0&sortDir=desc` | ❌ | Course reviews |
| POST | `/api/courses/{courseId}/reviews` | STUDENT | Submit review |
| PUT | `/api/courses/{courseId}/reviews/{reviewId}` | AUTH | Update own review |
| DELETE | `/api/courses/{courseId}/reviews/{reviewId}` | AUTH | Delete review |

### Categories
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/categories` | ❌ | All active categories |
| GET | `/api/categories/{id}` | ❌ | Category by ID |
| POST | `/api/categories` | ADMIN | Create category |
| PUT | `/api/categories/{id}` | ADMIN | Update category |
| DELETE | `/api/categories/{id}` | ADMIN | Soft-delete category |

### Files
| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/files/upload?lessonId=1` | INSTRUCTOR/ADMIN | Upload file (multipart) |
| GET | `/api/files/{storedFilename}/download` | AUTH | Download file |
| GET | `/api/files/lessons/{lessonId}` | AUTH | Get file info for lesson |
| DELETE | `/api/files/{fileId}` | INSTRUCTOR/ADMIN | Delete file |

---

## ⚡ Async Processes

| # | Class | Method | Trigger | Description |
|---|-------|--------|---------|-------------|
| 1 | `EmailNotificationService` | `sendEnrollmentConfirmationEmail` | On enrollment | Async email to student |
| 2 | `EmailNotificationService` | `sendCourseCompletionEmail` | Progress = 100% | Completion certificate email |
| 3 | `EmailNotificationService` | `sendWelcomeEmail` | On register | Welcome email to new user |
| 4 | `StatisticsService` | `recalculateCourseRating` | On review create/update/delete | Async rating recalculation |
| 5 | `StatisticsService` | `updateCourseStudentCount` | On enroll/drop | Async student count update |
| 6 | `StatisticsService` | `scheduledFullStatisticsRecalculation` | `@Scheduled` cron (02:00 daily) | Nightly full stats refresh |

---

## 🐳 Docker Setup

```bash
# 1. Clone and go to project folder
git clone https://github.com/YOUR_USERNAME/online-course-platform.git
cd online-course-platform

# 2. Copy and configure environment
cp .env.example .env

# 3. Build and start all services
docker-compose up --build -d

# 4. Check health
docker-compose ps
curl http://localhost:8080/api/actuator/health

# 5. Open Swagger UI
open http://localhost:8080/api/swagger-ui.html

# 6. View logs
docker-compose logs -f app

# Start with pgAdmin (dev profile)
docker-compose --profile dev up -d
# PgAdmin: http://localhost:5050  (admin@course.kz / admin)
```

---

## 🚀 Local Setup (without Docker)

```bash
# Prerequisites: Java 17+, Maven 3.9+, PostgreSQL 15+

# 1. Create database
psql -U postgres -c "CREATE DATABASE coursedb;"

# 2. Set environment variables or edit application.yml
export DB_USERNAME=postgres
export DB_PASSWORD=postgres

# 3. Run
mvn spring-boot:run

# Swagger UI: http://localhost:8080/api/swagger-ui.html
```

---

## 📋 Commit History Guide (20–25 commits)

```
Commit 01: Initial project setup — Spring Boot, pom.xml, application.yml
Commit 02: Entity layer — User, Course, Lesson entities
Commit 03: Entity layer — Enrollment, Review, Category, FileAttachment entities
Commit 04: Enums — Role, CourseStatus, EnrollmentStatus, LessonType
Commit 05: Repository layer — UserRepository, CourseRepository with JPQL queries
Commit 06: Repository layer — Lesson, Enrollment, Review, Category, FileAttachment repositories
Commit 07: DTO layer — Request DTOs with validation annotations
Commit 08: DTO layer — Response DTOs and generic ApiResponse wrapper
Commit 09: MapStruct mappers — User, Course, Lesson, Enrollment, Review, Category mappers
Commit 10: Exception handling — custom exceptions and GlobalExceptionHandler
Commit 11: Security — JWT utility (JwtUtil) and JWT auth filter
Commit 12: Security — UserDetails, UserDetailsService, SecurityConfig
Commit 13: Service layer — AuthService (register + login)
Commit 14: Service layer — CourseService with pagination, search, filtering
Commit 15: Service layer — LessonService, CategoryService
Commit 16: Service layer — EnrollmentService with progress tracking
Commit 17: Service layer — ReviewService with async rating recalculation
Commit 18: Async services — EmailNotificationService, StatisticsService (@Async + @Scheduled)
Commit 19: File storage — FileStorageService (upload, download, delete)
Commit 20: Controller layer — AuthController, CourseController, LessonController
Commit 21: Controller layer — EnrollmentController, ReviewController, CategoryController, FileController
Commit 22: Config — SwaggerConfig (OpenAPI 3), AsyncConfig, RequestLoggingFilter
Commit 23: Docker — Dockerfile (multistage), docker-compose.yml, .env.example
Commit 24: Logging — request/response filter, structured log patterns, log files
Commit 25: Final cleanup — README, .gitignore, .dockerignore, code review
```

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Language |
| Spring Boot | 3.2.3 | Framework |
| Spring Security | 6.x | Auth & Authorization |
| Spring Data JPA | 3.x | ORM / Repository |
| PostgreSQL | 15 | Database |
| JWT (jjwt) | 0.11.5 | Token generation |
| MapStruct | 1.5.5 | DTO mapping |
| Lombok | latest | Boilerplate reduction |
| SpringDoc OpenAPI | 2.3.0 | Swagger UI |
| Docker | latest | Containerization |

---

*Online Course Platform — Final Project by Bekzhanuly Nurmukhamed*
