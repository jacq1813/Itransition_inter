using Microsoft.AspNetCore.Authentication;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using LoginApp.Data;
using LoginApp.Models;

public class AccountController : Controller
{
    private readonly ApplicationDbContext _context;

    public AccountController(ApplicationDbContext context)
    {
        _context = context;
    }

    [HttpGet]
    public IActionResult Login()
    {
        return View();
    }

    [HttpPost]
    public async Task<IActionResult> Login(string email, string password)
    {
        var user = await _context.Users.FirstOrDefaultAsync(u => u.Email == email);
        
        if (user == null || user.Password != password)
        {
            ModelState.AddModelError("", "Invalid email or password");
            return View();
        }
        
        if (user.IsBlocked)
        {
            ModelState.AddModelError("", "Your account is blocked");
            return View();
        }

        // Update last login time
        user.LastLoginTime = DateTime.Now;
        await _context.SaveChangesAsync();

        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new Claim(ClaimTypes.Email, user.Email),
            new Claim(ClaimTypes.Name, user.Name)
        };

        var identity = new ClaimsIdentity(claims, "CookieAuth");
        var principal = new ClaimsPrincipal(identity);

        await HttpContext.SignInAsync("CookieAuth", principal);

        return RedirectToAction("Index", "Users");
    }

    [HttpGet]
    public IActionResult Register()
    {
        return View();
    }

    [HttpPost]
    public async Task<IActionResult> Register(User user)
    {
        if (ModelState.IsValid)
        {
            try
            {
                user.RegistrationTime = DateTime.Now;
                user.LastLoginTime = DateTime.Now;
                user.IsBlocked = false;
                
                _context.Users.Add(user);
                await _context.SaveChangesAsync();

                return RedirectToAction("Login");
            }
            catch (DbUpdateException ex) when (IsUniqueConstraintViolation(ex))
            {
                ModelState.AddModelError("Email", "Email already exists");
            }
            catch
            {
                ModelState.AddModelError("", "An error occurred while registering");
            }
        }
        
        return View(user);
    }

    private bool IsUniqueConstraintViolation(DbUpdateException ex)
    {
        return ex.InnerException is SqlException sqlEx && 
               (sqlEx.Number == 2601 || sqlEx.Number == 2627);
    }

    [Authorize]
    public async Task<IActionResult> Logout()
    {
        await HttpContext.SignOutAsync("CookieAuth");
        return RedirectToAction("Login");
    }
}