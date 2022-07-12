using System.Collections.Generic;

namespace Iv4xr.SpaceEngineers.UI
{
    public class FrameSnapshot
    {
        public InputSnapshot Input { get; set; }
        //public BlockSnapshot BlockSnapshot { get; set; }
        //public CameraSnapshot CameraSnapshot { get; set; }
    }
    
    public class InputSnapshot
    {
        public KeyboardSnapshot Keyboard { get; set; }
        public MouseSnapshot Mouse { get; set; }
        //public JoystickSnapshot Joystick { get; set; }
    }
    
    public class KeyboardSnapshot
    {
        public List<int> PressedKeys { get; set; }
        public List<char> Text { get; set; }
    }
    
    public class MouseSnapshot
    {
        public int CursorPositionX { get; set; }
        public int CursorPositionY { get; set; }
        public int X { get; set; }
        public int Y { get; set; }
        public int ScrollWheelValue { get; set; }

        public bool LeftButton { get; set; }
        public bool RightButton { get; set; }
        public bool MiddleButton { get; set; }
        public bool XButton1 { get; set; }
        public bool XButton2 { get; set; }
    }
}
