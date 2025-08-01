using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class User
{
    [Key]
    public int Id { get; set; }
    
    [Required]
    [StringLength(100)]
    public string Name { get; set; }
    
    [Required]
    [StringLength(100)]
    public string Email { get; set; }
    
    [Required]
    public string Password { get; set; }
    
    public DateTime LastLoginTime { get; set; }
    
    public DateTime RegistrationTime { get; set; }
    
    public bool IsBlocked { get; set; }
}