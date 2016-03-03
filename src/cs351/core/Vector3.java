package cs351.core;

/**
 * Java's Point classes don't let you set a new value for the individual
 * components, forcing you to create a new object even to make the smallest change.
 *
 * This class gets around this limitation.
 *
 * Note that this is bare-bones (cross product, dot product, etc. are left out) because
 * of the very limited way we use this class.
 */
final public class Vector3
{
  private double x, y, z;
  private float magnitude;
  private boolean calcMagnitude = true;

  public Vector3(double value)
  {
    set(value, value, value);
  }

  public Vector3(double x, double y, double z)
  {
    set(x, y, z);
  }

  public Vector3(Vector3 vec3)
  {
    set(vec3.x, vec3.y, vec3.z);
  }

  @Override
  public int hashCode()
  {
    // not very good but should be ok for now (I think)
    return (int)((10 * ((int)x ^ 10) / z) * (20 * ((int)y ^ 20) / x) * (30 * ((int)z ^ 30) / y));
  }

  @Override
  public boolean equals(Object other)
  {
    if (this == other) return true;
    else if (!(other instanceof Vector3)) return false;
    Vector3 vec3 = (Vector3)other;
    return this.x == vec3.x && this.y == vec3.y && this.z == vec3.z;
  }

  public void set(double x, double y, double z)
  {
    calcMagnitude = true;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void set(Vector3 vec3)
  {
    set(vec3.x, vec3.y, vec3.z);
  }

  public void setX(double x)
  {
    set(x, this.y, this.z);
  }

  public void setY(double y)
  {
    set(this.x, y, this.z);
  }

  public void setZ(double z)
  {
    set(this.x, this.y, z);
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }

  public double getZ()
  {
    return z;
  }

  public void normalize()
  {
    magnitude();
    x /= magnitude;
    y /= magnitude;
    z /= magnitude;
  }

  public double magnitude()
  {
    if (calcMagnitude) magnitude = (float)Math.sqrt(x * x + y * y + z * z);
    calcMagnitude = false;
    return magnitude;
  }

  public Vector3 subtract(Vector3 other)
  {
    return new Vector3(this.x - other.x, this.y - other.y, this.z - other.y);
  }

  public Vector3 add(Vector3 other)
  {
    return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public double dot(Vector3 other)
  {
    return this.x * other.x + this.y * other.y + this.z * other.z;
  }

  public Vector3 cross(Vector3 other)
  {
    return new Vector3(this.y * other.z - this.z * other.y,
                       this.z * other.x - this.x * other.z,
                       this.x * other.y - this.y * other.x);
  }
}
