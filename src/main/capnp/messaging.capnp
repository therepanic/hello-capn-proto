@0xdadebabe87654321;

using Java = import "/java.capnp";

$Java.package("com.example.capnp.messaging");
$Java.outerClassname("MessagingCapnp");

struct User {
  id @0 :UInt32;
  username @1 :Text;
}

struct Message {
  id @0 :UInt64;
  sender @1 :User;
  content @2 :Text;
}

struct Chat {
  id @0 :UInt64;
  title @1 :Text;
  messages @2 :List(Message);
}
