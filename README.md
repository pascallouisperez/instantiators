Kawala Instantiators
--------------------

Serializing Plain Old Java Objects (POJOs) is a surprisingly complicated task. There are many options out there, which all have pros-and-cons. For instance:

* Java serialization works out of the box for both serialization & deserialization, but fails to be backward/forward compatible.
* JSON (or XML) serialization leveraging [Gson](http://code.google.com/p/google-gson/), [Jackson](http://jackson.codehaus.org/) or [JsonMarshaller](http://code.google.com/p/jsonmarshaller/) require some level of annotations in the POJOs.

Instantiators is one more approach which centers around instantiating and destantiating, i.e. replacing new and an inverse of new. Instantiators constrain POJO constructors to be provably idempotent and provides everything else out-of-the-box.

### Introductory Example

Assume we have a simple UserMessage object which encapsulates two strings for the first and last name respectively.

    class UserMessage {
      UserMessage(String firstName, String lastName) { ...
    }

Using instantiators, you can simply instantiate this message using positional parameters via

    Instantiators
        .createInstantiator(UserMessage.class)
        .newInstance("John", "Doe")

or using named parameters

    Instantiators
        .createInstantiator(UserMessage.class)
        .newInstance(ImmutableMap.of(
            "firstName", "John",
            "lastName", "Doe"))

Destantiation is as simple

    Instantiators
        .createInstantiator(UserMessage.class)
        .fromInstance(new UserMessage("John", "Doe"))

In a typical usage, the appropriate instantior (`Instantiator<UserMessage>`) would be injected or made available through the environment.

### Value Objects

Instantiators revolve around value value objects and can instantiate and destantiate them. A value object is one that has an idempotent constructor. This simply means that constructor arguments are:

* ignored or
* stored as-is in fields (i.e. `this.field = argument;`)

Simple idempotent transformation such as boxing/unboxing are also supported and allowed.

Arguments to a value object can be either:

* base types (`int`, `boolean`, `String`, `Character`, etc.)
* other value objects
* objects for which a converter has been registered
* objects annotated by `@ConvertedBy`

#### Converters

Converters define one-to-one and onto functions from a type and String. They implement the interface:

    interface Converter<T> {
      String toString(T value);
      T fromString(String representation);
    }

Converters can be bound to specific types explicitly using an InstantiatorModule.

    Instantiators
        .createInstantiator(UserMessage.class, new AbstractInstantiatorModule() {
          void configure() {
            registerFor(URI.class).converter(UriConverter.class);
          }
        })
        .newInstance(“John”, “Doe”)

Alternatively, an `@ConvertedBy` annotation can also be placed on value objects to define a default converter. This overrides the out-of-the box behavior and lifts the constructor constraints.

#### Optional Arguments

Arguments can be additionally marked as being optional -- this is done using the `@Optional` annotation or by using an `Option<...>`. Optional arguments do not need to be provided when instantiating a value object, and their corresponding fields may be `null`.

Using the `@Optional` annotation allows you to specify a default value:

    UserMessage(String firstName, String lastName, @Optional(“42”) int age)