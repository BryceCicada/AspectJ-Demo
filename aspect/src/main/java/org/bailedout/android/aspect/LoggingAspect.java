package org.bailedout.android.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

/** Clean project after changing any pointcuts or joinpoints **/
@Aspect
public class LoggingAspect {

  private static boolean LOG_ON_CREATE = true;

  /** Example pointcut showing how we can enable or disable logging for particular methods */
  @Pointcut("execution(* onCreate(android.os.Bundle)) && if()")
  public static boolean onCreateMethod() {
    return LOG_ON_CREATE;
  }

  @Pointcut("onCreateMethod()" +
         "|| execution(* onStart())" +
         "|| execution(* onResume())" +
         "|| execution(* onConfigurationChanged(..))" +
         "|| execution(* onPause())" +
         "|| execution(* onStop())" +
         "|| execution(* onDestroy())")
  void activityLifecycleMethod() {
  }

  @Pointcut("execution(* onAttach(android.app.Activity))" +
         "|| onCreateMethod()" +
         "|| execution(* onCreateView(..))" +
         "|| execution(* onViewCreated(..))" +
         "|| execution(* onActivityCreated(..))" +
         "|| execution(* onActivityResult(..))" +
         "|| execution(* onStart())" +
         "|| execution(* onResume())" +
         "|| execution(* onPause())" +
         "|| execution(* onStop())" +
         "|| execution(* onDestroyView())" +
         "|| execution(* onDestroy())" +
         "|| execution(* onDetach())")
  void fragmentLifecycleMethod() {
  }

  @Before("activityLifecycleMethod() || fragmentLifecycleMethod()")
  public void beforeLifecycleMethod(JoinPoint joinPoint) {
    String tag = joinPoint.getTarget().getClass().getSimpleName();
    Log.d(tag, joinPoint.getSignature().getName() + " >>> (" + Arrays.toString(joinPoint.getArgs()) + ")");
  }

  @AfterReturning(pointcut="activityLifecycleMethod() || fragmentLifecycleMethod()", returning="retval")
  public void afterLifecycleMethodReturns(JoinPoint joinPoint, Object retval) {
    String tag = joinPoint.getTarget().getClass().getSimpleName();
    Log.d(tag, "<<< " + joinPoint.getSignature().getName() + ": " + retval);
  }

  @AfterThrowing(pointcut="activityLifecycleMethod() || fragmentLifecycleMethod()", throwing = "throwable")
  public void afterLifecycleMethodThrows(JoinPoint joinPoint, Object throwable) {
    String tag = joinPoint.getTarget().getClass().getSimpleName();
    Log.d(tag, "<<< " + joinPoint.getSignature().getName() + ": " + throwable);
  }
}
