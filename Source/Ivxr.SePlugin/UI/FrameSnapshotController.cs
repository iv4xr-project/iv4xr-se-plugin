using System;
using System.Collections.Generic;
using System.Linq;
using Iv4xr.SpaceEngineers.UI;
using VRage.Input;
using VRage.Input.Keyboard;

namespace Iv4xr.SePlugin.UI
{
    public class FrameSnapshotController : IFrameSnapshotController
    {
        public static MouseSnapshot EmptyMouse = new MouseSnapshot()
        {
            CursorPositionX = 0,
            CursorPositionY = 0,
            X = 0,
            Y = 0,
            ScrollWheelValue = 0,
            LeftButton = false,
            RightButton = false,
            MiddleButton = false,
            XButton1 = false,
            XButton2 = false,
        };

        public static KeyboardSnapshot EmptyKeyboard = new KeyboardSnapshot()
        {
            PressedKeys = new List<int>(),
            Text = new List<char>(),
        };

        private IMyInput Input => MyInput.Static;

        public FrameSnapshot GetCurrent()
        {
            return new FrameSnapshot
            {
                Input = new InputSnapshot()
                {
                    Mouse = GetMouseSnapshot(),
                    Keyboard = new KeyboardSnapshot()
                    {
                        PressedKeys = GetKeyboardSnapshot().Select(item => (int)item).ToList(),
                        Text = GetKeyboardTextSnapshot(),
                    }
                }
            };
        }

        private List<char> GetKeyboardTextSnapshot()
        {
            return Input.TextInput.ToList();
        }

        private List<MyKeys> GetKeyboardSnapshot()
        {
            return Enum.GetValues(typeof(MyKeys))
                    .Cast<MyKeys>()
                    .Where(key => Input.IsKeyPress(key))
                    .ToList();
        }

        private MouseSnapshot GetMouseSnapshot()
        {
            var state = Input.ActualMouseState;
            return new MouseSnapshot()
            {
                CursorPositionX = (int)Input.GetMousePosition().X,
                CursorPositionY = (int)Input.GetMousePosition().Y,
                X = state.X,
                Y = state.Y,
                LeftButton = state.LeftButton,
                MiddleButton = state.MiddleButton,
                RightButton = state.RightButton,
                ScrollWheelValue = state.ScrollWheelValue,
                XButton1 = state.XButton1,
                XButton2 = state.XButton2,
            };
        }

        public void SetCurrent(FrameSnapshot snapshot)
        {
            var input = snapshot.Input;
            var currentKeyboardState = RestoreState(input.Keyboard);
            var text = input.Keyboard.Text;
            var currentMouseState = RestoreState(input.Mouse);
            var currentJoystickState = new MyJoystickState(); //no cares right now

            Input.UpdateStates(currentKeyboardState, text, currentMouseState, currentJoystickState,
                currentMouseState.X, currentMouseState.Y);
        }

        private MyKeyboardState RestoreState(KeyboardSnapshot snp)
        {
            var snapshot = snp ?? EmptyKeyboard;
            var keyboardState = new MyKeyboardState();

            snapshot.PressedKeys.Select(
                i => (MyKeys)i
            ).ForEach(
                key => keyboardState.SetKey(key, true)
            );

            return keyboardState;
        }

        private MyMouseState RestoreState(MouseSnapshot snp)
        {
            var snapshot = snp ?? EmptyMouse;
            return new MyMouseState()
            {
                X = snapshot.X,
                Y = snapshot.Y,
                LeftButton = snapshot.LeftButton,
                MiddleButton = snapshot.MiddleButton,
                RightButton = snapshot.RightButton,
                ScrollWheelValue = snapshot.ScrollWheelValue,
                XButton1 = snapshot.XButton1,
                XButton2 = snapshot.XButton2,
            };
        }
    }
}
