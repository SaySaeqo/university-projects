#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_mso_lab_Lab4Activity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jintArray JNICALL
Java_mso_lab_Lab4Activity_sortFromJNI(
        JNIEnv* env,
        jobject /* this */,
        jintArray array) {

    if (array == nullptr) return nullptr;

    jsize len = env->GetArrayLength(array);
    if (len <= 1) return array;

    jint* body = new jint[len];
    env->GetIntArrayRegion(array, 0, len, body);

    for (int i = 0; i< len; i++){
        for (int j = i+1; j< len; j++){
            int a = body[i];
            int b = body[j];
            int maximum = std::max(a,b);
            int minimum = std::min(a,b);
            body[i] = minimum;
            body[j] = maximum;
        }
    }
    jintArray result = env->NewIntArray(len);
    if (result != nullptr) {
        env->SetIntArrayRegion(result, 0, len, body);
    }

    delete[] body;
    return result;
}