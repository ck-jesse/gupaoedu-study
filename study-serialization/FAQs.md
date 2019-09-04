# Java 序列化

#### 如果你的Serializable类包含一个不可序列化的成员，会发生什么？你是如何解决的？
    任何序列化该类的尝试都会因NotSerializableException而失败，但这可以通过在 Java中 为 static 设置瞬态(trancient)变量来轻松解决。

    Java 序列化相关的常见问题
    
    Java 序列化是一个重要概念, 但它很少用作持久性解决方案, 开发人员大多忽略了 Java 序列化 API。
    根据我的经验, Java 序列化在任何 Java核心内容面试中都是一个相当重要的话题, 在几乎所有的网面试中, 我都遇到过一两个 Java 序列化问题, 我
    看过一次面试, 在问几个关于序列化的问题之后候选人开始感到不自在, 因为缺乏这方面的经验。
    
    他们不知道如何在 Java 中序列化对象, 或者他们不熟悉任何 Java 示例来解释序列化, 忘记了诸如序列化在 Java 中如何工作, 什么是标记接口, 标记接口的目的是什么, 
    瞬态变量和可变变量之间的差异, 可序列化接口具有多少种方法, 在 Java 中,Serializable 和 Externalizable 有什么区别, 
    或者在引入注解之后, 为什么不用 @Serializable 注解或替换 Serializalbe 接口。


#### 关于Java序列化的10个面试问题

    大多数商业项目使用数据库或内存映射文件或只是普通文件, 来满足持久性要求, 只有很少的项目依赖于 Java 中的序列化过程。无论如何,这篇文章不是 Java 序列化教程或如何序列化在 Java 的对象, 但有关序列化机制和序列化 API 的面试问题, 这是值得去任何 Java 面试前先看看以免让一些未知的内容惊到自己。

    对于那些不熟悉 Java 序列化的人, Java 序列化是用来通过将对象的状态存储到带有.ser扩展名的文件来序列化 Java 中的对象的过程, 并且可以通过这个文件恢复重建 Java对象状态, 这个逆过程称为 deserialization。

#### 什么是 Java 序列化
    序列化是把对象改成可以存到磁盘或通过网络发送到其他运行中的 Java 虚拟机的二进制格式的过程, 并可以通过反序列化恢复对象状态. 
    Java 序列化API给开发人员提供了一个标准机制, 通过 java.io.Serializable 和 java.io.Externalizable 接口, 
    ObjectInputStream 及ObjectOutputStream 处理对象序列化. Java 程序员可自由选择基于类结构的标准序列化或是他们自定义的二进制格式, 
    通常认为后者才是最佳实践, 因为序列化的二进制文件格式成为类输出 API的一部分, 可能破坏 Java 中私有和包可见的属性的封装.

#### 如何序列化

    让 Java 中的类可以序列化很简单. 你的 Java 类只需要实现 java.io.Serializable 接口, JVM 就会把 Object 对象按默认格式序列化. 让一个类是可序列化的需要有意为之. 类可序列会可能为是一个长期代价, 可能会因此而限制你修改或改变其实现. 当你通过实现添加接口来更改类的结构时, 添加或删除任何字段可能会破坏默认序列化, 这可以通过自定义二进制格式使不兼容的可能性最小化, 但仍需要大量的努力来确保向后兼容性。序列化如何限制你更改类的能力的一个示例是 SerialVersionUID。

    如果不显式声明 SerialVersionUID, 则 JVM 会根据类结构生成其结构, 该结构依赖于类实现接口和可能更改的其他几个因素。假设你新版本的类文件实现的另一个接口, JVM 将生成一个不同的 SerialVersionUID 的, 当你尝试加载旧版本的程序序列化的旧对象时, 你将获得无效类异常 InvalidClassException。

#### 问题 1) Java 中的可序列化接口和可外部接口之间的区别是什么？
    这是 Java 序列化访谈中最常问的问题。下面是我的版本 Externalizable 给我们提供 writeExternal() 和 readExternal() 方法, 这让我们灵活地控制 Java 序列化机制, 而不是依赖于 Java 的默认序列化。正确实现 Externalizable 接口可以显著提高应用程序的性能。

#### 问题 2) 可序列化的方法有多少？如果没有方法,那么可序列化接口的用途是什么？

    可序列化 Serializalbe 接口存在于java.io包中,构成了 Java 序列化机制的核心。它没有任何方法, 在 Java 中也称为标记接口。当类实现 java.io.Serializable 接口时, 它将在 Java 中变得可序列化, 并指示编译器使用 Java 序列化机制序列化此对象。

#### 问题 3) 什么是 serialVersionUID ？如果你不定义这个, 会发生什么？

    我最喜欢的关于Java序列化的问题面试问题之一。serialVersionUID 是一个 private static final long 型 ID, 当它被印在对象上时, 它通常是对象的哈希码,你可以使用 serialver 这个 JDK 工具来查看序列化对象的 serialVersionUID。SerialVerionUID 用于对象的版本控制。也可以在类文件中指定 serialVersionUID。不指定 serialVersionUID的后果是,当你添加或修改类中的任何字段时, 则已序列化类将无法恢复, 因为为新类和旧序列化对象生成的 serialVersionUID 将有所不同。Java 序列化过程依赖于正确的序列化对象恢复状态的, ,并在序列化对象序列版本不匹配的情况下引发 java.io.InvalidClassException 无效类异常,了解有关 serialVersionUID 详细信息,请参阅这篇文章, 需要 FQ。

#### 问题 4) 序列化时,你希望某些成员不要序列化？你如何实现它？

    另一个经常被问到的序列化面试问题。这也是一些时候也问, 如什么是瞬态 trasient 变量, 瞬态和静态变量会不会得到序列化等,所以,如果你不希望任何字段是对象的状态的一部分, 然后声明它静态或瞬态根据你的需要, 这样就不会是在 Java 序列化过程中被包含在内。

#### 问题 5) 如果类中的一个成员未实现可序列化接口, 会发生什么情况？

    关于Java序列化过程的一个简单问题。如果尝试序列化实现可序列化的类的对象,但该对象包含对不可序列化类的引用,则在运行时将引发不可序列化异常 NotSerializableException, 这就是为什么我始终将一个可序列化警报(在我的代码注释部分中), 代码注释最佳实践之一, 指示开发人员记住这一事实, 在可序列化类中添加新字段时要注意。

#### 问题 6) 如果类是可序列化的, 但其超类不是, 则反序列化后从超级类继承的实例变量的状态如何？

    Java 序列化过程仅在对象层次都是可序列化结构中继续, 即实现 Java 中的可序列化接口, 并且从超级类继承的实例变量的值将通过调用构造函数初始化, 在反序列化过程中不可序列化的超级类。一旦构造函数链接将启动, 就不可能停止, 因此, 即使层次结构中较高的类实现可序列化接口, 也将执行构造函数。正如你从陈述中看到的, 这个序列化面试问题看起来非常棘手和有难度, 但如果你熟悉关键概念, 则并不难。

#### 问题 7) 是否可以自定义序列化过程, 或者是否可以覆盖 Java 中的默认序列化过程？

    答案是肯定的, 你可以。我们都知道,对于序列化一个对象需调用 ObjectOutputStream.writeObject(saveThisObject), 并用 ObjectInputStream.readObject() 读取对象, 但 Java 虚拟机为你提供的还有一件事, 是定义这两个方法。如果在类中定义这两种方法, 则 JVM 将调用这两种方法, 而不是应用默认序列化机制。你可以在此处通过执行任何类型的预处理或后处理任务来自定义对象序列化和反序列化的行为。

    需要注意的重要一点是要声明这些方法为私有方法, 以避免被继承、重写或重载。由于只有 Java 虚拟机可以调用类的私有方法, 你的类的完整性会得到保留, 并且 Java 序列化将正常工作。在我看来, 这是在任何 Java 序列化面试中可以问的最好问题之一, 一个很好的后续问题是, 为什么要为你的对象提供自定义序列化表单？

#### 问题 8) 假设新类的超级类实现可序列化接口, 如何避免新类被序列化？

    在 Java 序列化中一个棘手的面试问题。如果类的 Super 类已经在 Java 中实现了可序列化接口, 那么它在 Java 中已经可以序列化, 因为你不能取消接口, 它不可能真正使它无法序列化类, 但是有一种方法可以避免新类序列化。为了避免 Java 序列化,你需要在类中实现 writeObject() 和 readObject() 方法, 并且需要从该方法引发不序列化异常NotSerializableException。这是自定义 Java 序列化过程的另一个好处, 如上述序列化面试问题中所述, 并且通常随着面试进度, 它作为后续问题提出。

#### 问题 9) 在 Java 中的序列化和反序列化过程中使用哪些方法？

    这是很常见的面试问题, 在序列化基本上面试官试图知道: 你是否熟悉 readObject() 的用法、writeObject()、readExternal() 和 writeExternal()。Java 序列化由java.io.ObjectOutputStream类完成。该类是一个筛选器流, 它封装在较低级别的字节流中, 以处理序列化机制。要通过序列化机制存储任何对象, 我们调用 ObjectOutputStream.writeObject(savethisobject), 并反序列化该对象, 我们称之为 ObjectInputStream.readObject()方法。调用以 writeObject() 方法在 java 中触发序列化过程。关于 readObject() 方法, 需要注意的一点很重要一点是, 它用于从持久性读取字节, 并从这些字节创建对象, 并返回一个对象, 该对象需要类型强制转换为正确的类型。

#### 问题 10) 假设你有一个类,它序列化并存储在持久性中, 然后修改了该类以添加新字段。如果对已序列化的对象进行反序列化, 会发生什么情况？

    这取决于类是否具有其自己的 serialVersionUID。正如我们从上面的问题知道, 如果我们不提供 serialVersionUID, 则 Java 编译器将生成它, 通常它等于对象的哈希代码。通过添加任何新字段, 有可能为该类新版本生成的新 serialVersionUID 与已序列化的对象不同, 在这种情况下, Java 序列化 API 将引发 java.io.InvalidClassException, 因此建议在代码中拥有自己的 serialVersionUID, 并确保在单个类中始终保持不变。

#### 11) Java序列化机制中的兼容更改和不兼容更改是什么？

    真正的挑战在于通过添加任何字段、方法或删除任何字段或方法来更改类结构, 方法是使用已序列化的对象。根据 Java 序列化规范, 添加任何字段或方法都面临兼容的更改和更改类层次结构或取消实现的可序列化接口, 有些接口在非兼容更改下。对于兼容和非兼容更改的完整列表, 我建议阅读 Java 序列化规范。

#### 12) 我们可以通过网络传输一个序列化的对象吗？

    是的 ,你可以通过网络传输序列化对象, 因为 Java 序列化对象仍以字节的形式保留, 字节可以通过网络发送。你还可以将序列化对象存储在磁盘或数据库中作为 Blob。

#### 13) 在 Java 序列化期间,哪些变量未序列化？

    这个问题问得不同, 但目的还是一样的, Java开发人员是否知道静态和瞬态变量的细节。由于静态变量属于类, 而不是对象, 因此它们不是对象状态的一部分, 因此在 Java 序列化过程中不会保存它们。由于 Java 序列化仅保留对象的状态,而不是对象本身。瞬态变量也不包含在 Java 序列化过程中, 并且不是对象的序列化状态的一部分。在提出这个问题之后,面试官会询问后续内容, 如果你不存储这些变量的值, 那么一旦对这些对象进行反序列化并重新创建这些变量, 这些变量的价值是多少？这是你们要考虑的。